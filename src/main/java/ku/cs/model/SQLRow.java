package ku.cs.model;

import ku.cs.utility.ProjectUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLRow {
    private String tableName;
    private List<String> primaryKey;
    private String primaryKeyValue;
    private List<String> columns;
    private List<Object> values;
    private HashMap<String, Object> valuesMap = new HashMap<>();

    public SQLRow(List<String> columns, List<Object> values) {
        this("null", columns, values);
    }

    public SQLRow(String tableName, List<String> columns, List<Object> values) {
        this("tableName", null, "null", columns, values);
    }

    //HashMap
    public SQLRow(String tableName, List<String> primaryKey, String primaryKeyValue, List<String> columns, HashMap<String, Object> valuesMap) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.primaryKeyValue = primaryKeyValue;
        this.columns = columns;
        values = new ArrayList<>();
        for (String column: columns) values.add(valuesMap.get(column));
        if (columns.size() != values.size()) {
            ProjectUtility.debug("SQLRow[constructor]: [Mapper] Cannot match columns with values -> " + columns.toString());
            ProjectUtility.debug("SQLRow[constructor]: [Mapper] Cannot map values -> " + values.toString());
            return;
        }

    }

    //List
    public SQLRow(String tableName, List<String> primaryKey, String primaryKeyValue, List<String> columns, List<Object> values) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.primaryKeyValue = primaryKeyValue;
        this.columns = columns;
        this.values = values;
        if (columns.size() != values.size()) {
            ProjectUtility.debug("SQLRow[constructor]: [Mapper] Cannot match columns with values -> " + columns.toString());
            ProjectUtility.debug("SQLRow[constructor]: [Mapper] Cannot map values -> " + values.toString());
            return;
        }
        for (int i = 0; i < columns.size(); i++) {
            valuesMap.put(columns.get(i), values.get(i));
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPrimaryKeyValue() {
        return primaryKeyValue;
    }

    public void setPrimaryKeyValue(String primaryKeyValue) {
        this.primaryKeyValue = primaryKeyValue;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public HashMap<String, Object> getValuesMap() {
        return valuesMap;
    }

    public void setValuesMap(HashMap<String, Object> valuesMap) {
        this.valuesMap = valuesMap;
    }

    public String toString() {
        String msg_out = "SQLRow[tableName]: " + tableName + " {"
                + "\n\tprimaryKey: " + primaryKey
                + "\n\t, primaryKeyValue: " + primaryKeyValue
                + "\n\t, valuesMap {";
        for (String key : valuesMap.keySet()) {
            msg_out += "\n\t\t" + key + ": " + valuesMap.get(key);
        }
        return msg_out + "\n\t}\n}";
    }


}
