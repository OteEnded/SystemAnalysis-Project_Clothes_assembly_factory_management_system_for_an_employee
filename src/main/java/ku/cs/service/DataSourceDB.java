package ku.cs.service;

import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.utility.JdbcConnector;
import ku.cs.utility.ProjectUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSourceDB {

    public static int inUse = 0;

    public static Connection getConnection(boolean isInUse){
        if (isInUse) {
            inUse += 1;
            JdbcConnector.connect();
        } else if (inUse > 0) inUse -= 1;
        else JdbcConnector.disconnect();
        return JdbcConnector.getConnection();
    }

    public static SQLRow query(String query) throws SQLException {
        JdbcConnector.connect();
        SQLRow sqlRow = toSQLRow(JdbcConnector.query(query));
        sqlRow.setTableName(extractTableName(query));
        JdbcConnector.disconnect();
        return sqlRow;
    }

    public static SQLRow toSQLRow(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        List<String> columns = new ArrayList<>();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columns.add(metaData.getColumnName(i));
//            System.out.println("Column Name: " + metaData.getColumnName(i));
//            System.out.println("Column Type: " + metaData.getColumnTypeName(i));
//            System.out.println("Column Size: " + metaData.getPrecision(i));
        }
        List<Object> values = new ArrayList<>();
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                values.add(rs.getObject(i));
            }
        }
        ProjectUtility.debug("columns: " + columns);
        ProjectUtility.debug("values: " + values);
        return new SQLRow(columns, values);
    }

    // Simple method to extract the table name from an SQL query
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

    public static int createTable(SQLTable sqlTable) throws SQLException {
        JdbcConnector.connect();
        int affectedRows = JdbcConnector.update(sqlTable.getCreateQuery());
        JdbcConnector.disconnect();
        return affectedRows;
    }

    public static int dropTable(SQLTable sqlTable) throws SQLException {
        return dropTable(sqlTable.getName());
    }

    public static int dropTable(String tableName) throws SQLException {
        JdbcConnector.connect();
        int affectedRows = JdbcConnector.update("DROP TABLE IF EXISTS " + tableName);
        JdbcConnector.disconnect();
        return affectedRows;
    }

    public static int dropTableAll() throws SQLException {
        JdbcConnector.connect();

        // Execute a SQL query to retrieve foreign key constraints
        ResultSet resultSet = JdbcConnector.query("SELECT TABLE_NAME, CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'FOREIGN KEY'");

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            String constraintName = resultSet.getString("CONSTRAINT_NAME");

            boolean isTableExist = false;
            for (String myTable : getTableList()) {
                if (myTable.equals(tableName)) {
                    isTableExist = true;
                    break;
                }
            }
            if (!isTableExist) continue;


            // Generate DROP FOREIGN KEY statement
            String dropStatement = "ALTER TABLE " + tableName + " DROP FOREIGN KEY " + constraintName;
            System.out.println("Dropping foreign key constraint: " + constraintName + " in table " + tableName);

            // Execute the DROP FOREIGN KEY statement
            JdbcConnector.update(dropStatement);
        }

        System.out.println("All foreign key constraints dropped successfully.");

        int affectedRows = 0;
        for (String tableName : getTableList()) {
            System.out.println("Dropping table: " + tableName);

            // Execute a DROP TABLE statement for each table
            affectedRows += dropTable(tableName);
        }

        System.out.println("All tables dropped successfully.");
        JdbcConnector.disconnect();
        return affectedRows;
    }

    public static List<String> getTableList() throws SQLException {
        List<String> tableList = new ArrayList<>();
        for (Object table: query("show tables").getValues()){
            tableList.add((String) table);
        }
        return tableList;
    }

    public static int emptyTable(SQLTable sqlTable) throws SQLException {
        return emptyTable(sqlTable.getName());
    }

    public static int emptyTable(String tableName) throws SQLException {
        JdbcConnector.connect();
        int affectedRows = JdbcConnector.update("TRUNCATE " + tableName);
        JdbcConnector.disconnect();
        return affectedRows;
    }

    public static SQLRow load(SQLTable sqlTable) throws SQLException {
        return load(sqlTable.getName());
    }

    public static SQLRow load(String tableName) throws SQLException {
        return query("SELECT * FROM " + tableName);
    }

    public static int exePrepare(PreparedStatement preparedStatement) throws SQLException {
        int rowsUpdated = 0;
        if (inUse > 0) {
            rowsUpdated = preparedStatement.executeUpdate();
            inUse -= 1;
            getConnection(false);
        }
        else ProjectUtility.debug("Invalid preparedStatement usage with JDBCO.");
        return rowsUpdated;
    }

//    public static boolean insert(SQLRow sqlRow) throws SQLException {
//        JdbcConnector.connect();
//        Statement statement = getConnection().createStatement();
//        String query = "INSERT INTO " + sqlRow.getTableName() + " (";
//        for (String column : sqlRow.getColumns()) {
//            query += column + ", ";
//        }
//        query = query.substring(0, query.length() - 2);
//        query += ") VALUES (";
//        for (Object value : sqlRow.getValues()) {
//            query += "'" + value + "', ";
//        }
//        query = query.substring(0, query.length() - 2);
//        query += ")";
//        System.out.println(query);
//        return statement.executeUpdate(query) > 0;
//    }
}
