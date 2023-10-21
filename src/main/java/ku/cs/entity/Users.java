package ku.cs.entity;

import ku.cs.model.SQLColumn;
import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.model.User;
import ku.cs.service.DataSourceDB;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

public class Users implements Entity {
    private static final SQLTable sqlTable = new SQLTable("Users");
    static {
        SQLColumn sqlColumn;

        sqlColumn = new SQLColumn();
        sqlColumn.setName("id");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey(true);
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("sign_up_date");
        sqlColumn.setType("date");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("age");
        sqlColumn.setType("int");
        sqlTable.addColumObj(sqlColumn);
        sqlColumn = new SQLColumn();

        sqlColumn.setName("name");
        sqlColumn.setType("varchar(255)");
        sqlTable.addColumObj(sqlColumn);
    }

    public static SQLTable getSqlTable() {
        return sqlTable;
    }

    private static HashMap<String, User> data = null;
    static {
        data = new HashMap<>();
    }

    public static void load(){
        data = new HashMap<>();
//        for (SQLColumn column : sqlTable.getColums()) {
//            data.put(column.getName(), null);
//        }
    }

    public static HashMap<String, User> getData() {
        return data;
    }

    public static void setData(HashMap<String, User> data) {
        Users.data = data;
    }

    public static String getNewId(){
        if (data == null) load();
        if (getData().size() == 0) return "Users1";
        else {
            ArrayList<String> newId = new ArrayList<>(getData().keySet());
            Collections.sort(newId);
            return "Users" + String.valueOf(Integer.parseInt(newId.get(getData().size() - 1)) + 1);
        }
    }

    public static void addData(User user) {
        if (data == null) load();
        if (user.getId() == null) {
            user.setId(getNewId());
        }
        data.put(user.getId(), user);
    }

    public static int save(User user) throws SQLException, ParseException {
        List<String> columnsStr = new ArrayList<>();
        for (SQLColumn sqlcolumn: sqlTable.getColumns()){
            columnsStr.add(sqlcolumn.getName());
        }
        List<String> primaryKeysStr = new ArrayList<>();
        for (SQLColumn sqlcolumn: sqlTable.getPrimaryKeys()){
            primaryKeysStr.add(sqlcolumn.getName());
        }
        SQLRow sqlRow = new SQLRow(sqlTable.getName(), primaryKeysStr, user.getPrimaryKey(), columnsStr, user.getData());
        HashMap<String, Object> dataOnDB = DataSourceDB.load(sqlTable.getName()).getValuesMap();
        for (String key: dataOnDB.keySet()){
            if (dataOnDB.get("id").equals(user.getId())){
                return DataSourceDB.exePrepare(sqlTable.getUpdateQuery(sqlRow));
            }
        }
        return DataSourceDB.exePrepare(sqlTable.getInsertQuery(sqlRow));
    }

    public static HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        for (SQLColumn column : sqlTable.getColumns()) {
            map.put(column.getName(), null);
        }
        return map;
    }
}
