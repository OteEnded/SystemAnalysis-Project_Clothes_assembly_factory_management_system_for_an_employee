package ku.cs.utility;

import ku.cs.model.SQLRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcConnector {

    private static final String db_URL = "jdbc:mysql://mowcodeserver.eastus.cloudapp.azure.com:3306/sa_longname";
//    private static final String db_URL = "jdbc:mysql://localhost:3306/sa_testing";
    private static final String db_username = "project";
    private static final String db_password = "Ku81Cs36";
    private static Connection db_connection = null;

    private static int connectionStack = 0;

    //get connection
    public static Connection getConnection() {
        return db_connection;
    }


    public static boolean connect() {
        try {
            // check connection
            if (db_connection != null) {
                connectionStack += 1;
                ProjectUtility.debug("JdbcConnector[connect]: db_connection is not null, connectionStack ->", String.valueOf(connectionStack));
                return false;
            }
            // setup
            Class.forName("com.mysql.cj.jdbc.Driver");
            db_connection = DriverManager.getConnection(db_URL, db_username, db_password);
            if (db_connection != null) {
                return true;
            }
            ProjectUtility.debug("JdbcConnector[connect]: db_connection is not null");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //disconnect
    public static boolean realDisconnect(){
        try {
            // close connection
            if (connectionStack > 0) {
                connectionStack -= 1;
                ProjectUtility.debug("JdbcConnector[disconnect]: disconnected a connectionStack, connectionStack ->", connectionStack);
                return false;
            }
            db_connection.close();
            db_connection = null;
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean disconnectAll(){
        while (db_connection != null) {
            realDisconnect();
        }
        return true;
    }

    public static boolean disconnect(){
        return true;
    }


    public static boolean isTableExist(String tableName) throws SQLException {
        DatabaseMetaData dbm = db_connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        return tables.next();
    }

    public static ResultSet query(String query) throws SQLException {
        Statement statement = db_connection.createStatement();
        ProjectUtility.debug("JdbcConnector[query]:", query);
        return statement.executeQuery(query);
    }

    public static int update(String query) throws SQLException {
        Statement statement = db_connection.createStatement();
        ProjectUtility.debug("JdbcConnector[update]:", query);
        return statement.executeUpdate(query);
    }

    public static DatabaseMetaData getMetadata() throws SQLException {
        return db_connection.getMetaData();
    }
}
