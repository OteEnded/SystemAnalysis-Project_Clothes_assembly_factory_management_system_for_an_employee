package ku.cs.utility;

import ku.cs.model.Product;
import ku.cs.model.Row;
import ku.cs.model.SQLColumn;
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
        for (SQLColumn column : sqlTable.getColumns()) {
            map.put(column.getName(), null);
        }
        return map;
    }

    public static List<String> verifyObjectByTable(SQLTable sqlTable, Row row){
        List<String> error = new ArrayList<>();
        for (SQLColumn column : sqlTable.getColumns()) {
            if (column.isNotNull() && row.getData().get(column.getName()) == null) {
                error.add("Column " + column.getName() + " is null");
            }
        }
        return error;
    }
}
