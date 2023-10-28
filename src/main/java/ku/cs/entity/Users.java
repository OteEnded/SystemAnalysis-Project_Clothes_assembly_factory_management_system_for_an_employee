package ku.cs.entity;

import ku.cs.model.SQLColumn;
import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.model.User;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class Users implements Entity {

    private static final SQLTable sqlTable = new SQLTable("Users");
    static {
        SQLColumn sqlColumn;

        sqlColumn = new SQLColumn();
        sqlColumn.setName("id");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey();
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

    public static HashMap<String, User> load() throws SQLException {
        return load(true);
    }

    // load data from database
    public static HashMap<String, User> load(boolean updateUsers) throws SQLException {
        HashMap<String, User> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = DataSourceDB.load(sqlTable);
        for (SQLRow sqlRow : sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new User(sqlRow.getValuesMap()));
        }
        if (updateUsers) data = dataFromDB;
        return dataFromDB;
    }

    public static HashMap<String, User> getData() {
        return data;
    }

    public static void setData(HashMap<String, User> data) {
        Users.data = data;
    }

    // แก้เป็น U 00001 แล้วเพิ่มเลขไปเรื่อยๆ
    public static String getNewId() throws SQLException {
        if (data == null) load();
        if (data.size() == 0) return EntityUtility.idFormatter(sqlTable, 1);
        ArrayList<String> oldId = new ArrayList<>(getData().keySet());
        Collections.sort(oldId);
        int oldLastId = Integer.parseInt((oldId.get(getData().size() - 1).substring(1,6)));
        return EntityUtility.idFormatter(sqlTable, oldLastId + 1);
    }

    public static void addData(User user) throws SQLException {
        ProjectUtility.debug("Users[addData]: adding user ->", user);
        if (data == null) load();
        if (user.getId() == null) {
            String newId = getNewId();
            user.setId(newId);
        }
        data.put(user.getId(), user);
        ProjectUtility.debug("Users[addData]: added new user with id ->", user.getId(), "=", user);
    }

    public static boolean isNew(User user){
        return isNew(user.getId());
    }

    public static boolean isNew(String id){
        if (id == null) return true;
        EntityUtility.checkId(sqlTable, id);
        return !data.containsKey(id);
    }

    public static int save(User user) throws SQLException, ParseException {
        ProjectUtility.debug("Users[save]: saving user ->", user);

        List<String> columnsStr = new ArrayList<>();
        for (SQLColumn sqlcolumn: sqlTable.getColumns()){
            columnsStr.add(sqlcolumn.getName());
        }
        if (isNew(user)){
            addData(user);
            return DataSourceDB.exePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable.getName(), user.getPrimaryKeys(), columnsStr, user.getData())));
        }
        return DataSourceDB.exePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable.getName(), user.getPrimaryKeys(), columnsStr, user.getData())));
    }

    public static int delete(User user) throws SQLException, ParseException {
        ProjectUtility.debug("Users[delete]: deleting user ->", user);
        List<String> columnsStr = new ArrayList<>();
        for (SQLColumn sqlcolumn: sqlTable.getColumns()){
            columnsStr.add(sqlcolumn.getName());
        }
        data.remove(user.getId());
        return DataSourceDB.exePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable.getName(), user.getPrimaryKeys(), columnsStr, user.getData())));
    }

    public static int delete(String id) throws SQLException, ParseException {
        User user = new User(id);
        return delete(user);
    }

    public static HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        for (SQLColumn column : sqlTable.getColumns()) {
            map.put(column.getName(), null);
        }
        return map;
    }

}
