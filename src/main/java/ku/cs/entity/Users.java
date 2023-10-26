package ku.cs.entity;

import ku.cs.model.SQLColumn;
import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.model.User;
import ku.cs.service.DataSourceDB;
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
        SQLRow sqlRow = DataSourceDB.load(sqlTable);
        ProjectUtility.debug(sqlRow);
        int rowSize = sqlRow.getValues().size() / sqlRow.getColumns().size();
        for (int i = 0; i < rowSize; i++){
            ProjectUtility.debug("i:", i);
            User user = new User();
            for (int j = 0; j < sqlRow.getColumns().size(); j++){
                ProjectUtility.debug("j:", j);
                ProjectUtility.debug("Adding:", i*sqlRow.getColumns().size() + j, sqlRow.getColumns().get(j), sqlRow.getValues().get(i*sqlRow.getColumns().size() + j));
                user.addData(sqlRow.getColumns().get(j), sqlRow.getValues().get(i*sqlRow.getColumns().size() + j));
            }
            dataFromDB.put(user.getId(), user);
            ProjectUtility.debug(dataFromDB);
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
        if (getData().size() == 0) return "U" + String.format("%05d", 1);
        else {
            ArrayList<String> oldId = new ArrayList<>(getData().keySet());
            Collections.sort(oldId);
            int oldLastId = Integer.parseInt((oldId.get(getData().size() - 1).substring(1,6)));
            String newId = "U" + String.format("%05d", oldLastId + 1);
            return newId;
        }
    }

    public static void addData(User user) throws SQLException {
        if (data == null) load();
        if (user.getId() == null) {
            String newId = getNewId();
            user.setId(newId);
            ProjectUtility.debug("New id: " + user.getId());
        }
        data.put(user.getId(), user);
    }

    public static boolean isNewUser(User user){
        return isNewUser(user.getId());
    }

    public static boolean isNewUser(String id){
        if (id == null) return false;
        if (data.containsKey(id)) return false;
        checkId(id);
        return true;
    }

    public static boolean isIdValid(String id){
        if (id.length() != 6) return false;
        if (id.startsWith("U")) return false;
        if (!id.substring(1,6).matches("\\d+")) return false;
        return true;
    }

    public static void checkId(String id){
        if (!isIdValid(id)) throw new RuntimeException("Id: " + id + "is Invalid!");
    }

    public static int save(User user) throws SQLException, ParseException {
        if (isNewUser(user)) addData(user);

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
        if (!isNewUser(user)){
            return DataSourceDB.exePrepare(sqlTable.getUpdateQuery(sqlRow));
        }

        return DataSourceDB.exePrepare(sqlTable.getInsertQuery(sqlRow));
    }

//    public static int updateDB(){
//
//    }

    public static int delete(User user) throws SQLException, ParseException {
        List<String> columnsStr = new ArrayList<>();
        for (SQLColumn sqlcolumn: sqlTable.getColumns()){
            columnsStr.add(sqlcolumn.getName());
        }
        List<String> primaryKeysStr = new ArrayList<>();
        for (SQLColumn sqlcolumn: sqlTable.getPrimaryKeys()){
            primaryKeysStr.add(sqlcolumn.getName());
        }
        SQLRow sqlRow = new SQLRow(sqlTable.getName(), primaryKeysStr, user.getPrimaryKey(), columnsStr, user.getData());
        return DataSourceDB.exePrepare(sqlTable.getDeleteQuery(sqlRow));
    }

    public static HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        for (SQLColumn column : sqlTable.getColumns()) {
            map.put(column.getName(), null);
        }
        return map;
    }
}
