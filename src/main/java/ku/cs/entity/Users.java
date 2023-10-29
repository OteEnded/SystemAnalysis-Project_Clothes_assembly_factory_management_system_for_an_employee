package ku.cs.entity;

import ku.cs.model.*;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class Users {

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

    private static HashMap<String, User> data;

    public static HashMap<String, User> getData() throws SQLException{
        if(data == null) load();
        return data;
    }

    public static void setData(HashMap<String, User> data) {
        Users.data = data;
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

    public static String getJoinedPrimaryKeys(User user) {
        SQLRow sqlRow = new SQLRow(sqlTable, user);
        return sqlRow.getJoinedPrimaryKeys();
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
        if (getJoinedPrimaryKeys(user) == null || getJoinedPrimaryKeys(user) == "") user.setId(getNewId());
        data.put(user.getId(), user);
        ProjectUtility.debug("Users[addData]: added new user with id ->", user.getId(), "=", user);
    }

    public static boolean isNew(User user) throws SQLException {
        return isNew(user.getId());
    }

    public static boolean isNew(String id) throws SQLException {
        if (id == null) load();
        if (data.isEmpty()) return true;
        return !data.containsKey(id);
    }

    public static int save(User user) throws SQLException, ParseException {
        ProjectUtility.debug("Users[save]: saving user ->", user);

        if (isNew(user)){
            addData(user);
            return DataSourceDB.exePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, user)));
        }
        return DataSourceDB.exePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, user)));
    }

    public static int delete(String id) throws SQLException, ParseException {
        User user = new User();
        user.load(id);
        return delete(user);
    }


    public static int delete(User user) throws SQLException, ParseException {
        ProjectUtility.debug("Users[delete]: deleting user ->", user);
        if (isNew(user)) throw new RuntimeException("Users[delete]: Can not delete user that is not in database");
        data.remove(user.getId());
        return DataSourceDB.exePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, user)));
    }

    public static HashMap<String, Object> filter;
    public static void addFilter(String column, Object value) {
        if (filter == null) filter = new HashMap<>();
        filter.put(column, value);
    }
    public static void setFilter(HashMap<String, Object> filter) {
        Users.filter = filter;
    }

    public static HashMap<String, Object> getFilter() {
        return filter;
    }

    public static HashMap<String, User> getFilteredData(HashMap<String, Object> filter) throws SQLException {
        setFilter(filter);
        return getFilteredData();
    }

    public static HashMap<String, User> getFilteredData() throws SQLException {
        if (filter == null) throw new RuntimeException("Users[getFilteredData]: filter is null, Please set filter first or get all data without filter using -> Users.getData()");
        if (data == null) load();
        HashMap<String, User> filteredData = new HashMap<>();
        for (User user: getData().values()) {
            boolean isFiltered = true;
            for (String column: filter.keySet()) {
                if(!user.getData().get(column).equals(filter.get(column))) {
                    isFiltered = false;
                    break;
                }
            }
            if (isFiltered) filteredData.put(user.getId(), user);
        }
        filter = null;
        return filteredData;
    }
}
