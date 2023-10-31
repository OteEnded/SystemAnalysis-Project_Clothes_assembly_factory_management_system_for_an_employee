package ku.cs.testingarea_codeexample;

import ku.cs.service.DBMigration;
import ku.cs.service.DBSeedAndLoad;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class Example1_SetUp {

    /**
    how to connect to database
     */
    public static boolean connectDB() {
        boolean isCompleted = false;

        /*
        use ProjectUtility.connectDB() to connect to database,
        this method will return boolean of connection status
         */
        isCompleted = ProjectUtility.connectDB();
        return isCompleted;
    }

    /**
    how to do DB migration (create table)
     */
    public static void migrateDB() throws SQLException {

        /*
        if you want to drop all table before migrate, set this to true
        (to clear all data before do testing)
        */
        boolean wantToDropAllTable = false;

        // use DBMigration.migrate() to migrate database
        DBMigration.migrate(wantToDropAllTable);

    }

    /**
    how to do DB seeding (add starter mockup data to database)
     */
    public static void seedDB() throws SQLException, ParseException {

        // use DBMigration.seed() to seed database
        DBSeedAndLoad.seed();
    }

    /**
     * how to do DB loading (load data from database to entity buffer)
     */
    public static void loadDB() throws SQLException, ParseException {

        // use DBMigration.load() to load database
        DBSeedAndLoad.load();

        // and you can use quick load to load data faster (load only Work and Product)
        DBSeedAndLoad.quickLoad();

    }


    /**
     * you can test your code here
     */
    public static void main(String[] args) {

    }
}
