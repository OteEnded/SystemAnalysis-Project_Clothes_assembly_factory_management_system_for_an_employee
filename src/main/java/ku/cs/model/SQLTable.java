package ku.cs.model;

import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// SQLTable is a class that represents a table in a database
public class SQLTable {
    private String name;
    private List<SQLColumn> columns = new ArrayList<>();

    public SQLTable(String name) {
        this.name = name;
    }

    public void addColumObj(SQLColumn SQLColumn) {
        columns.add(SQLColumn);
    }

    public String getName() {
        return name;
    }

    public List<SQLColumn> getColumnsValues() {
        return columns;
    }

    public SQLColumn getColum(){
        return new SQLColumn();
    }

    public List<SQLColumn> getPrimaryKeys(){
        List<SQLColumn> primaryKeys = new ArrayList<>();
        for (SQLColumn colum: columns){
            if (colum.isPrimaryKey()) primaryKeys.add(colum);
        }
        return primaryKeys;
    }

    public List<SQLColumn> getForeignKeys(){
        List<SQLColumn> foreignKeys = new ArrayList<>();
        for (SQLColumn colum: columns){
            if (colum.isForeignKey()) foreignKeys.add(colum);
        }
        return foreignKeys;
    }

    public SQLColumn getColumnByName(String name){
        for (SQLColumn column: columns){
            if (column.getName().equals(name)){
                return column;
            }
        }
        return null;
    }

    public String getCreateQuery() {
        String sql = "CREATE TABLE " + name + " (";
        boolean hasOnePrimarykey = false;
        List<SQLColumn> primaryKeys = getPrimaryKeys();
        if (primaryKeys.size() == 1) {
            hasOnePrimarykey = true;
            sql += primaryKeys.get(0).getCreateColum() + ", ";
        }
        else{
            for (SQLColumn colum : primaryKeys) {
                sql += colum.getCreateColum();
                sql = sql.substring(0, sql.length() - 12) + ", ";
//                sql += " NOT NULL, ";
            }
        }
        for (SQLColumn colum : columns) {
            if (colum.isPrimaryKey()) continue;
            sql += colum.getCreateColum() + ", ";
        }
        if (!hasOnePrimarykey) {
            sql += "PRIMARY KEY (";
            for (SQLColumn colum : primaryKeys) {
                sql += colum.getName() + ", ";
            }
            sql = sql.substring(0, sql.length() - 2);
            sql += "), ";
        }
        for (SQLColumn colum : getForeignKeys()) {
            sql += colum.getCreateForeignKey() + ", ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += ");";
        return sql;
    }

    private PreparedStatement typePutter(Object value, String type, int index, PreparedStatement preparedStatement) throws SQLException, ParseException {
        if (value == null) {
            preparedStatement.setNull(index, Types.NULL);
            return preparedStatement;
        }
        switch (type){
            case "int":
                preparedStatement.setInt(index, (int) value);
                break;
            case "bigint":
                preparedStatement.setLong(index, (long) value);
                break;
            case "float":
                preparedStatement.setFloat(index, (float) value);
                break;
            case "double":
                preparedStatement.setDouble(index, (double) value);
                break;
            case "boolean":
                preparedStatement.setBoolean(index, (boolean) value);
                break;
            case "date":
                try {
                    preparedStatement.setDate(index, (Date) value);
                }
                catch (Exception e){
                    ProjectUtility.debug("SQLTable[typePutter]: got exception ->", e.getMessage());
                    ProjectUtility.debug("SQLTable[typePutter]: date format might not Date, trying to parse...");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date(dateFormat.parse((String) value).getTime());
                    preparedStatement.setDate(index, date);
                    ProjectUtility.debug("SQLTable[typePutter]: date parsed successfully, date ->", date);
                }
                break;
            default:
                preparedStatement.setString(index, (String) value);
                break;
        }
        return preparedStatement;
    }

    public PreparedStatement getInsertQuery(SQLRow sqlRow) throws SQLException, ParseException {
        ProjectUtility.debug("SQLTable[getInsertQuery]: building insert query for ->", sqlRow);
        String sql = "INSERT INTO " + name + " (";
        for (SQLColumn column: columns){
            sql += column.getName() + ", ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += ") VALUES (";
        for (SQLColumn column: columns){
            sql += "?, ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += ")";
        PreparedStatement preparedStatement = DataSourceDB.getConnection(true).prepareStatement(sql);
        ProjectUtility.debug("SQLTable[getInsertQuery]: built prepareStatement ->", sql);
        for (int i = 1; i <= columns.size(); i++){
            ProjectUtility.debug("SQLTable[getInsertQuery]: adding prepareStatement parameter {", i, ":", sqlRow.getValues().get(i-1), "}");
            typePutter(sqlRow.getValues().get(i - 1), columns.get(i - 1).getType(), i, preparedStatement);
        }
        return preparedStatement;
    }

    public PreparedStatement getUpdateQuery(SQLRow sqlRow) throws SQLException, ParseException {
        ProjectUtility.debug("SQLTable[getUpdateQuery]: building update query for ->", sqlRow);
        String sql = "UPDATE " + name + " SET ";
        for (SQLColumn column: columns){
            sql += column.getName() + " = ?, ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += " WHERE ";
        for (SQLColumn column: getPrimaryKeys()){
            sql += column.getName() + " = ? AND ";
        }
        sql = sql.substring(0, sql.length() - 5);
        PreparedStatement preparedStatement = DataSourceDB.getConnection(true).prepareStatement(sql);
        ProjectUtility.debug("SQLTable[getUpdateQuery]: built prepareStatement ->", sql);
        for (int i = 1; i <= columns.size(); i++){
            ProjectUtility.debug("SQLTable[getUpdateQuery]: adding prepareStatement parameter {", i, ":", sqlRow.getValues().get(i-1), "}");
            typePutter(sqlRow.getValues().get(i - 1), columns.get(i - 1).getType(), i, preparedStatement);
        }
        int j = 0;
        for (int i = columns.size()+1; i <= columns.size()+getPrimaryKeys().size(); i++){
            ProjectUtility.debug("SQLTable[getUpdateQuery]: adding prepareStatement parameter {", i, ":", sqlRow.getPrimaryKeys().get(getPrimaryKeys().get(j).getName()), "}");
            typePutter(sqlRow.getPrimaryKeys().get(getPrimaryKeys().get(j).getName()), getPrimaryKeys().get(j).getType(), i, preparedStatement);
            j++;
        }
        return preparedStatement;
    }

    public PreparedStatement getDeleteQuery(SQLRow sqlRow) throws SQLException, ParseException {
        ProjectUtility.debug("SQLTable[getDeleteQuery]: building delete query for ->", sqlRow);
        String sql = "DELETE FROM " + name + " WHERE ";
        for (SQLColumn column: getPrimaryKeys()){
            sql += column.getName() + " = ? AND ";
        }
        sql = sql.substring(0, sql.length() - 5);
        PreparedStatement preparedStatement = DataSourceDB.getConnection(true).prepareStatement(sql);
        ProjectUtility.debug("SQLTable[getDeleteQuery]: built prepareStatement ->", sql);
        int j = 0;
        for (int i = 1; i <= getPrimaryKeys().size(); i++){
            ProjectUtility.debug("SQLTable[getDeleteQuery]: adding prepareStatement parameter {", i, ":", sqlRow.getPrimaryKeys().get(getPrimaryKeys().get(j).getName()), "}");
            typePutter(sqlRow.getPrimaryKeys().get(getPrimaryKeys().get(j).getName()), getPrimaryKeys().get(j).getType(), i, preparedStatement);
            j++;
        }
        return preparedStatement;
    }

    public List<SQLRow> getAll() throws SQLException {
        return DataSourceDB.query("SELECT * FROM " + name + " LIMIT 1000");
    }

    public List<SQLRow> getWhere(String column, Object value) throws SQLException, ParseException {
        if (getColumnByName(column) == null) throw new SQLException("SQLTable[getWhere]: column " + column + " not found in table " + this);
        String sql = "SELECT * FROM " + name + " WHERE " + column + " = ?";
        PreparedStatement preparedStatement = DataSourceDB.getConnection(true).prepareStatement(sql);
        typePutter(value, getColumnByName(column).getType(), 1, preparedStatement);
        return DataSourceDB.exeQueryPrepare(preparedStatement);
    }

    public List<SQLRow> getWhere(HashMap<String, Object> filter) throws SQLException, ParseException {
        String sql = "SELECT * FROM " + name + " WHERE ";
        for (String column: filter.keySet()){
            if (getColumnByName(column) == null) throw new SQLException("SQLTable[getWhere]: column " + column + " not found in table " + this);
            sql += column + " = ? AND ";
        }
        sql = sql.substring(0, sql.length() - 5);

        PreparedStatement preparedStatement = DataSourceDB.getConnection(true).prepareStatement(sql);
        int i = 1;
        for (String column: filter.keySet()){
            typePutter(filter.get(column), getColumnByName(column).getType(), i, preparedStatement);
            i++;
        }
        return DataSourceDB.exeQueryPrepare(preparedStatement);
    }

    public SQLRow getFindOne(HashMap<String, Object> filter) throws SQLException, ParseException {
        List<SQLRow> found = getWhere(filter);
        if (found.isEmpty()) throw new SQLException("SQLTable[getFindOne]: cannot find data with filter -> " + filter);
        if (found.size() > 1) ProjectUtility.debug("SQLTable[getFindOne]: !!WARNING!! found more than one data ->", found);
        return found.get(0);
    }

    public List<SQLRow> getSortedBy(String... selectedColumns) throws SQLException {
        String sql = "SELECT * FROM " + name + " ORDER BY ";
        for (String column: selectedColumns){
            if (getColumnByName(column) == null) throw new SQLException("SQLTable[getSortedBy]: column " + column + " not found in table " + this);
            sql += column + ", ";
        }
        sql = sql.substring(0, sql.length() - 2);

        return DataSourceDB.query(sql);
    }

    public List<SQLRow> getColumnsValues(String... selectedColumns) throws SQLException {
        String sql = "SELECT ";
        for (String column: selectedColumns){
            if (getColumnByName(column) == null) throw new SQLException("SQLTable[getColumnsValues]: column " + column + " not found in table " + this);
            sql += column + ", ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += " FROM " + name;

        return DataSourceDB.query(sql);
    }

    @Override
    public String toString() {
        String msg_out =  "Object-SQLTable[" + name + " with " + columns.size() + " column(s)] {";
        for (SQLColumn colum: columns){
            msg_out += "\n\t" + colum.toString();
        }
        return msg_out+"\n}";
    }
}
