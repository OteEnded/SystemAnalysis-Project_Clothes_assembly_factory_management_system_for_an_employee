package ku.cs.model;

import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                sql = sql.substring(0, sql.length() - 12);
                sql += " NOT NULL, ";
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

    public PreparedStatement getInsertQuery(SQLRow sqlRow) throws SQLException, ParseException {
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
        ProjectUtility.debug(sql);
        ProjectUtility.debug(sqlRow.getValues());
        for (int i = 1; i <= columns.size(); i++){
            ProjectUtility.debug("i=",i);
            ProjectUtility.debug(sqlRow.getValues().get(i-1));
            switch (columns.get(i-1).getType()){
                case "int":
                    preparedStatement.setInt(i, (int) sqlRow.getValues().get(i-1));
                    break;
                case "bigint":
                    preparedStatement.setLong(i, (long) sqlRow.getValues().get(i-1));
                    break;
                case "float":
                    preparedStatement.setFloat(i, (float) sqlRow.getValues().get(i-1));
                    break;
                case "double":
                    preparedStatement.setDouble(i, (double) sqlRow.getValues().get(i-1));
                    break;
                case "boolean":
                    preparedStatement.setBoolean(i, (boolean) sqlRow.getValues().get(i-1));
                    break;
                case "date":
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date(dateFormat.parse((String) sqlRow.getValues().get(i-1)).getTime());
                    preparedStatement.setDate(i, date);
                    break;
                default:
                    preparedStatement.setString(i, (String) sqlRow.getValues().get(i-1));
                    break;
            }
        }
        return preparedStatement;
    }

    public PreparedStatement getUpdateQuery(SQLRow sqlRow) throws SQLException, ParseException {
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
        ProjectUtility.debug(sql);
        ProjectUtility.debug(sqlRow.getValues());
        for (int i = 1; i <= columns.size(); i++){
            ProjectUtility.debug("i=",i);
            ProjectUtility.debug(sqlRow.getValues().get(i-1));
            switch (columns.get(i-1).getType()){
                case "int":
                    preparedStatement.setInt(i, (int) sqlRow.getValues().get(i-1));
                    break;
                case "bigint":
                    preparedStatement.setLong(i, (long) sqlRow.getValues().get(i-1));
                    break;
                case "float":
                    preparedStatement.setFloat(i, (float) sqlRow.getValues().get(i-1));
                    break;
                case "double":
                    preparedStatement.setDouble(i, (double) sqlRow.getValues().get(i-1));
                    break;
                case "boolean":
                    preparedStatement.setBoolean(i, (boolean) sqlRow.getValues().get(i-1));
                    break;
                case "date":
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date(dateFormat.parse((String) sqlRow.getValues().get(i-1)).getTime());
                    preparedStatement.setDate(i, date);
                    break;
                default:
                    preparedStatement.setString(i, (String) sqlRow.getValues().get(i-1));
                    break;
            }
        }
        for (int i = columns.size()+1; i <= columns.size()+getPrimaryKeys().size(); i++){
            ProjectUtility.debug("i=",i);
            ProjectUtility.debug(sqlRow.getPrimaryKeyValue());
            preparedStatement.setString(i, (String) sqlRow.getPrimaryKeyValue());
        }
        return preparedStatement;
    }

    @Override
    public String toString() {
        String msg_out =  "SQLTable[name(columns.count())]: " + name + "(" + columns.size() + ") {";
        for (SQLColumn colum: columns){
            msg_out += "\n\t" + colum.toString();
        }
        return msg_out+"\n}";
    }
}
