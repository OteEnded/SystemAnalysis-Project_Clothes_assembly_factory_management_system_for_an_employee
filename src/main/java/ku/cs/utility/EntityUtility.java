package ku.cs.utility;

import ku.cs.model.Row;
import ku.cs.model.SQLColumn;
import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EntityUtility {
    public static String idFormatter(int id){
        return String.format("%05d", id);
    }

    public static String idFormatter(SQLTable sqlTable, int id){
        return sqlTable.getName().charAt(0) + idFormatter(id);
    }

    public static boolean isIdValid(SQLTable sqlTable, String id){
        if (id.length() != 6) return false;
        if (id.charAt(0) != sqlTable.getName().charAt(0)) return false;
        return id.substring(1, 6).matches("\\d+");
    }

    public static void checkId(SQLTable sqlTable, String id){
        if (isIdValid(sqlTable, id)) return;
        throw new RuntimeException("Id: " + id + "is Invalid!");
    }

    public static HashMap<String, Object> getMap(SQLTable sqlTable) {
        HashMap<String, Object> map = new HashMap<>();
        for (SQLColumn column : sqlTable.getColumnsValues()) {
            map.put(column.getName(), null);
        }
        return map;
    }

    public static List<String> verifyRowByTable(SQLTable sqlTable, Row row){
        List<String> error = new ArrayList<>();
        for (SQLColumn column : sqlTable.getColumnsValues()) {
            if (column.isNotNull() && row.getData().get(column.getName()) == null) {
                error.add("Column " + column.getName() + " is null");
            }
        }
        return error;
    }

    public static String getNewId(SQLTable sqlTable) throws SQLException {
        ProjectUtility.debug("EntityUtility[getNewId]: generating new id for table -> " + sqlTable);
        List<SQLColumn> primaryKeys = sqlTable.getPrimaryKeys();
        if (primaryKeys.size() != 1) throw new RuntimeException("EntityUtility[getNewId]: cannot get new id from table with multiple primary keys -> " + sqlTable);
        String idColumnName = primaryKeys.get(0).getName();
        List<SQLRow> ids = sqlTable.getColumnsValues(idColumnName);
        List<String> oldIds = new ArrayList<>();
        ProjectUtility.debug("EntityUtility[getNewId]: old ids -> " + ids);
        for (SQLRow sqlRow: ids) oldIds.add(sqlRow.getValuesMap().get(idColumnName).toString());
        if (oldIds.isEmpty()) return idFormatter(sqlTable, 1);
        Collections.sort(oldIds);
        int oldLastId = Integer.parseInt((oldIds.get(oldIds.size() - 1).substring(1,6)));
        return idFormatter(sqlTable, oldLastId + 1);
    }
}
