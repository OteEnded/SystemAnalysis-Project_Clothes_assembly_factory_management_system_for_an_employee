package ku.cs.service;

import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.utility.JdbcConnector;
import ku.cs.utility.ProjectUtility;

import java.sql.*;
import java.util.ArrayList;
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

    public static int createTable(SQLTable sqlTable) throws SQLException {
        ProjectUtility.debug("DataSourceDB[createTable]: trying to create table ->", sqlTable.getName());
        ProjectUtility.taskTimerStart();
        JdbcConnector.connect();
        int affectedRows = JdbcConnector.update(sqlTable.getCreateQuery());
        JdbcConnector.disconnect();
        ProjectUtility.debug("DataSourceDB[createTable]: executed, affectedRows ->", affectedRows, ", time ->", ProjectUtility.getFormattedTaskTimerResult());
        return affectedRows;
    }

    public static int dropTable(SQLTable sqlTable) throws SQLException {
        return dropTable(sqlTable.getName());
    }

    public static int dropTable(String tableName) throws SQLException {
        return dropTable(tableName, false);
    }

    public static int dropTable(String tableName, boolean force) throws SQLException {
        ProjectUtility.debug("DataSourceDB[dropTable]: trying to drop table ->", tableName);
        ProjectUtility.taskTimerStart();
        JdbcConnector.connect();
        int affectedRows = 0;
        SQLException error = null;
        try {
            if (force) JdbcConnector.update("SET FOREIGN_KEY_CHECKS = 0");
            affectedRows += JdbcConnector.update("DROP TABLE IF EXISTS " + tableName);
        }
        catch (SQLException e){
            ProjectUtility.debug("DataSourceDB[dropTable]: Error while dropping table ->", tableName);
            error = e;
        }
        finally {
            if (force) JdbcConnector.update("SET FOREIGN_KEY_CHECKS = 1");
        }
        JdbcConnector.disconnect();
        if (error != null) throw error;
        ProjectUtility.debug("DataSourceDB[dropTable]: executed, affectedRows ->", affectedRows, ", time ->", ProjectUtility.getFormattedTaskTimerResult());
        return affectedRows;
    }

    public static int dropTableAll() throws SQLException {
        ProjectUtility.debug("DataSourceDB[dropTableAll]: trying to drop all tables");
        JdbcConnector.connect();

        int affectedRows = 0;
        for (String tableName: getTableList()){
            affectedRows += dropTable(tableName, true);
        }

        JdbcConnector.disconnect();
        ProjectUtility.debug("DataSourceDB[dropTableAll]: executed, affectedRows ->", affectedRows);
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
        ProjectUtility.debug("DataSourceDB[emptyTable]: trying to empty table ->", tableName);
        ProjectUtility.taskTimerStart();
        JdbcConnector.connect();
        int affectedRows = JdbcConnector.update("TRUNCATE " + tableName);
        JdbcConnector.disconnect();
        ProjectUtility.debug("DataSourceDB[emptyTable]: executed, affectedRows ->", affectedRows, ", time ->", ProjectUtility.getFormattedTaskTimerResult());
        return affectedRows;
    }

    public static void withdrawConnection() throws SQLException {
        if (inUse > 0) {
            inUse -= 1;
            getConnection(false);
        }
        else ProjectUtility.debug("DataSourceDB[withdrawConnection]: Invalid preparedStatement usage with JDBCO.");
    }

    public static int exeUpdatePrepare(PreparedStatement preparedStatement) throws SQLException {
        ProjectUtility.debug("DataSourceDB[exeUpdatePrepare]: trying to execute ->", preparedStatement);
        ProjectUtility.taskTimerStart();
        int rowsUpdated = preparedStatement.executeUpdate();
        withdrawConnection();
        ProjectUtility.debug("DataSourceDB[exeUpdatePrepare]: executed, rowsUpdated ->", rowsUpdated, ", time ->", ProjectUtility.getFormattedTaskTimerResult());
        return rowsUpdated;
    }

    public static List<SQLRow> query(String query) throws SQLException {
        ProjectUtility.debug("DataSourceDB[query]: trying to query ->", query);
        ProjectUtility.taskTimerStart();
        JdbcConnector.connect();
        List<SQLRow> sqlRows = SQLRow.castRS(JdbcConnector.query(query));
        JdbcConnector.disconnect();
        ProjectUtility.debug("DataSourceDB[query]: executed, rows found ->", sqlRows.size(), ", time ->", ProjectUtility.getFormattedTaskTimerResult());
        return sqlRows;
    }

    public static List<SQLRow> exeQueryPrepare(PreparedStatement preparedStatement) throws SQLException {
        ProjectUtility.debug("DataSourceDB[exeQueryPrepare]: trying to execute query ->", preparedStatement);
        ProjectUtility.taskTimerStart();
        List<SQLRow> sqlRows = null;
        sqlRows = SQLRow.castRS(preparedStatement.executeQuery());
        withdrawConnection();
        ProjectUtility.debug("DataSourceDB[exeQueryPrepare]: executed, rows found ->", sqlRows.size(), ", time ->", ProjectUtility.getFormattedTaskTimerResult());
        return sqlRows;
    }

}
