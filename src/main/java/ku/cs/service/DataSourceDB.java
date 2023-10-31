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

    public static DatabaseMetaData getDBMetaData() throws SQLException {
        return JdbcConnector.getMetadata();
    }

    public static List<SQLRow> query(String query) throws SQLException {
        JdbcConnector.connect();
        List<SQLRow> sqlRows = SQLRow.castRS(JdbcConnector.query(query));
        JdbcConnector.disconnect();
        return sqlRows;
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

        int affectedRows = 0;
        affectedRows += dropTable("DailyRecords");
        affectedRows += dropTable("MaterialUsages");
        affectedRows += dropTable("Materials");
        affectedRows += dropTable("Works");
        affectedRows += dropTable("Products");
        affectedRows += dropTable("Users");

//
//        // Execute a SQL query to retrieve foreign key constraints
//        ResultSet resultSet = JdbcConnector.query("SELECT TABLE_NAME, CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'FOREIGN KEY'");
//
//        while (resultSet.next()) {
//            String tableName = resultSet.getString("TABLE_NAME");
//            String constraintName = resultSet.getString("CONSTRAINT_NAME");
//
//            boolean isTableExist = false;
//            for (String myTable : getTableList()) {
//                if (myTable.equals(tableName)) {
//                    isTableExist = true;
//                    break;
//                }
//            }
//            if (!isTableExist) continue;
//
//
//            // Generate DROP FOREIGN KEY statement
//            String dropStatement = "ALTER TABLE " + tableName + " DROP FOREIGN KEY " + constraintName;
//            System.out.println("Dropping foreign key constraint: " + constraintName + " in table " + tableName);
//
//            // Execute the DROP FOREIGN KEY statement
//            JdbcConnector.update(dropStatement);
//        }
//
//        System.out.println("All foreign key constraints dropped successfully.");
//
//        int affectedRows = 0;
//        for (String tableName : getTableList()) {
//            System.out.println("Dropping table: " + tableName);
//
//            // Execute a DROP TABLE statement for each table
//            affectedRows += dropTable(tableName);
//        }
//
//        System.out.println("All tables dropped successfully.");
        JdbcConnector.disconnect();
        return affectedRows;
    }

    public static List<String> getTableList() throws SQLException {
        List<String> tableList = new ArrayList<>();
        for (SQLRow sqlRow: query("show tables")){
            tableList.add((String) sqlRow.getValues().get(0));
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

    public static List<SQLRow> load(SQLTable sqlTable) throws SQLException {
        return load(sqlTable.getName());
    }

    public static List<SQLRow> load(String tableName) throws SQLException {
        return query("SELECT * FROM " + tableName);
    }

    public static int exePrepare(PreparedStatement preparedStatement) throws SQLException {
        ProjectUtility.debug("DataSourceDB[exePrepare]: trying to execute ->", preparedStatement);
        int rowsUpdated = 0;
        if (inUse > 0) {
            rowsUpdated = preparedStatement.executeUpdate();
            inUse -= 1;
            getConnection(false);
        }
        else ProjectUtility.debug("DataSourceDB[exePrepare]: Invalid preparedStatement usage with JDBCO.");
        ProjectUtility.debug("DataSourceDB[exePrepare]: executed, rowsUpdated ->", rowsUpdated);
        return rowsUpdated;
    }

}
