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
        JdbcConnector.connect();
        int affectedRows = JdbcConnector.update(sqlTable.getCreateQuery());
        JdbcConnector.disconnect();
        return affectedRows;
    }

    public static int dropTable(SQLTable sqlTable) throws SQLException {
        return dropTable(sqlTable.getName());
    }

    public static int dropTable(String tableName) throws SQLException {
        return dropTable(tableName, false);
    }

    public static int dropTable(String tableName, boolean force) throws SQLException {
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
        return affectedRows;
    }

    public static int dropTableAll() throws SQLException {
        JdbcConnector.connect();

        int affectedRows = 0;
        for (String tableName: getTableList()){
            affectedRows += dropTable(tableName, true);
        }

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

    public static int exeUpdatePrepare(PreparedStatement preparedStatement) throws SQLException {
        ProjectUtility.debug("DataSourceDB[exeUpdatePrepare]: trying to execute ->", preparedStatement);
        int rowsUpdated = 0;
        if (inUse > 0) {
            rowsUpdated = preparedStatement.executeUpdate();
            inUse -= 1;
            getConnection(false);
        }
        else ProjectUtility.debug("DataSourceDB[exeUpdatePrepare]: Invalid preparedStatement usage with JDBCO.");
        ProjectUtility.debug("DataSourceDB[exeUpdatePrepare]: executed, rowsUpdated ->", rowsUpdated);
        return rowsUpdated;
    }

    public static List<SQLRow> query(String query) throws SQLException {
        ProjectUtility.debug("DataSourceDB[query]: trying to query ->", query);
        JdbcConnector.connect();
        List<SQLRow> sqlRows = SQLRow.castRS(JdbcConnector.query(query));
        JdbcConnector.disconnect();
        return sqlRows;
    }

    public static List<SQLRow> exeQueryPrepare(PreparedStatement preparedStatement) throws SQLException {
        ProjectUtility.debug("DataSourceDB[exeQueryPrepare]: trying to execute query ->", preparedStatement);
        List<SQLRow> sqlRows = null;
        if (inUse > 0) {
            sqlRows = SQLRow.castRS(preparedStatement.executeQuery());
            inUse -= 1;
            getConnection(false);
        }
        else ProjectUtility.debug("DataSourceDB[exeQueryPrepare]: Invalid preparedStatement usage with JDBCO.");
        return sqlRows;
    }

}
