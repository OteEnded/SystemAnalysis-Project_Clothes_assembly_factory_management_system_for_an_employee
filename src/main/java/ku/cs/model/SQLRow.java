package ku.cs.model;

import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// อันนี้เอาไว้เก็บข้อมูลที่ดึงมากจาก sql เป็นแถวๆ และเอาไว้เก็บข้อมูลที่จะเอาไปเขียนลง sql
public class SQLRow {
    private String tableName;
    private HashMap<String, Object> primaryKeys;
    private List<String> columns;
    private List<Object> values;
    private HashMap<String, Object> valuesMap;

    //don't know much info
    public SQLRow(List<String> columns, List<Object> values) {
        this("notSet", columns, values);
    }

    public SQLRow(String tableName, List<String> columns, List<Object> values) {
        this(tableName, null, columns, values);
    }

    //List
    public SQLRow(String tableName, HashMap<String, Object> primaryKeys, List<String> columns, List<Object> values) {
        this.tableName = tableName;
        this.primaryKeys = primaryKeys;
        this.columns = columns;
        this.values = values;
        if (columns.size() != values.size()) {
            ProjectUtility.debug("SQLRow[constructor]: [Mapper] Cannot match columns with values -> " + columns.toString());
            ProjectUtility.debug("SQLRow[constructor]: [Mapper] Cannot map values -> " + values.toString());
            return;
        }
        valuesMap = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            valuesMap.put(columns.get(i), values.get(i));
        }
    }

    //HashMap
    public SQLRow(String tableName, HashMap<String, Object> primaryKeys, List<String> columns, HashMap<String, Object> valuesMap) {
        this.tableName = tableName;
        this.primaryKeys = primaryKeys;
        this.columns = columns;
        this.valuesMap = valuesMap;
        values = new ArrayList<>();
        for (String column: columns) values.add(valuesMap.get(column));
    }

    //SQLTable, RowInterface
    public SQLRow(SQLTable sqlTable , Row row) {
        this.tableName = sqlTable.getName();
        this.primaryKeys = row.getPrimaryKeys();
        List<String> columns = new ArrayList<>();
        for (SQLColumn sqlColumn: sqlTable.getColumns()) columns.add(sqlColumn.getName());
        this.columns = columns;
        this.valuesMap = row.getData();
        values = new ArrayList<>();
        for (String column: columns) values.add(valuesMap.get(column));
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public HashMap<String, Object> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(HashMap<String, Object> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public String getJoinedPrimaryKeys() {
        ProjectUtility.debug("SQLRow[getJoinedPrimaryKeys]: getting joined primaryKeys of sqlRow ->", this);
        List<String> primaryKeyList = new ArrayList<>();
        for (String key : columns) {
            if (primaryKeys.containsKey(key)) primaryKeyList.add(valuesMap.get(key).toString());
        }
        return String.join("|", primaryKeyList);
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

    public static List<SQLRow> castRS(ResultSet resultSet) throws SQLException {
        ProjectUtility.debug("SQLRow[castRS]: casting from resultSet...");
        if (resultSet == null) {
            ProjectUtility.debug("SQLRow[castRS]: resultSet is null");
            return null;
        }
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        String tableName = ProjectUtility.capitalize(resultSetMetaData.getTableName(1));
        ProjectUtility.debug("SQLRow[castRS]: found tableName ->", tableName);
        List<String> columns = new ArrayList<>();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columns.add(resultSetMetaData.getColumnName(i));
            ProjectUtility.debug("SQLRow[castRS]: found Column Name, Type, Size ->",
                    resultSetMetaData.getColumnName(i),
                    resultSetMetaData.getColumnTypeName(i),
                    resultSetMetaData.getPrecision(i)
            );
        }
        ProjectUtility.debug("SQLRow[castRS]: all columns found ->", columns);
        List<Object> values = new ArrayList<>();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                values.add(resultSet.getObject(i));
            }
        }
        ProjectUtility.debug("SQLRow[castRS]: all values found ->", values);

        DatabaseMetaData dbMetaData = DataSourceDB.getDBMetaData();
        ResultSet primaryKeys = dbMetaData.getPrimaryKeys(null, null, tableName);
        List<String> primaryKeyList = new ArrayList<>();
        while (primaryKeys.next()) {
            primaryKeyList.add(primaryKeys.getString("COLUMN_NAME"));
        }
        ProjectUtility.debug("SQLRow[castRS]: all primaryKeyList found ->", primaryKeyList);

        List<SQLRow> sqlRows = new ArrayList<>();
        int rowAmount = values.size() / columns.size();
        for (int i = 0; i < rowAmount; i++) {
            List<String> rowColumns = new ArrayList<>();
            List<Object> rowValues = new ArrayList<>();
            for (int j = 0; j < columns.size(); j++) {
                rowColumns.add(columns.get(j));
                rowValues.add(values.get(i * columns.size() + j));
            }
            SQLRow sqlRow = new SQLRow(tableName, rowColumns, rowValues);
            if (primaryKeyList.size() > 0) {
                HashMap<String, Object> primaryKeysMap = new HashMap<>();
                for (String key : primaryKeyList) {
                    primaryKeysMap.put(key, sqlRow.getValuesMap().get(key));
                }
                sqlRow.setPrimaryKeys(primaryKeysMap);
            }
            sqlRows.add(sqlRow);
        }
        ProjectUtility.debug("SQLRow[castRS]: all sqlRows found ->\n", sqlRows);

        return sqlRows;
    }

    public static String extractTableName(String query) {
        // This is a very basic example and may not work for all SQL queries
        // You might need to improve the regex or parsing logic based on your SQL query patterns
        String tableName = "";
        String[] parts = query.split("\\s");
        for (String part : parts) {
            if (part.equalsIgnoreCase("FROM")) {
                int index = Arrays.asList(parts).indexOf(part);
                if (index < parts.length - 1) {
                    tableName = parts[index + 1];
                    break;
                }
            }
        }
        return tableName;
    }

    @Override
    public String toString() {
        String msg_out = "Object-SQLRow[tableName]: " + tableName + " {"
                + "\n\tprimaryKey: " + primaryKeys
                + "\n\t, valuesMap {";
        for (String key : valuesMap.keySet()) {
            msg_out += "\n\t\t" + key + ": " + valuesMap.get(key);
        }
        return msg_out + "\n\t}\n}";
    }

}
