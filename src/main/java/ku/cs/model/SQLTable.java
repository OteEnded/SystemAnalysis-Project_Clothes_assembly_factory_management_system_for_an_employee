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

    public List<SQLColumn> getColumns() {
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
            preparedStatement = typePutter(sqlRow.getValues().get(i-1), columns.get(i-1).getType(), i, preparedStatement);
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
            preparedStatement = typePutter(sqlRow.getValues().get(i-1), columns.get(i-1).getType(), i, preparedStatement);
        }
        int j = 0;
        for (int i = columns.size()+1; i <= columns.size()+getPrimaryKeys().size(); i++){
            ProjectUtility.debug("SQLTable[getUpdateQuery]: adding prepareStatement parameter {", i, ":", sqlRow.getPrimaryKeys().get(getPrimaryKeys().get(j).getName()), "}");
            preparedStatement = typePutter(sqlRow.getPrimaryKeys().get(getPrimaryKeys().get(j).getName()), getPrimaryKeys().get(j).getType(), i, preparedStatement);
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
            preparedStatement = typePutter(sqlRow.getPrimaryKeys().get(getPrimaryKeys().get(j).getName()), getPrimaryKeys().get(j).getType(), i, preparedStatement);
            j++;
        }
        return preparedStatement;
    }

    public SQLColumn getColumnByName(String name){
        for (SQLColumn column: columns){
            if (column.getName().equals(name)){
                return column;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String msg_out =  "Object-SQLTable[name(columns.count())]: " + name + "(" + columns.size() + ") {";
        for (SQLColumn colum: columns){
            msg_out += "\n\t" + colum.toString();
        }
        return msg_out+"\n}";
    }
}
