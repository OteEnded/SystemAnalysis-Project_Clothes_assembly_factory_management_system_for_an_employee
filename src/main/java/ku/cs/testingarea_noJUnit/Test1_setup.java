package ku.cs.testingarea_noJUnit;

import ku.cs.service.DBMigration;
import ku.cs.service.DBSeedAndLoad;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;

public class Test1_setup {
    public static void main(String[] args) throws SQLException, IllegalAccessException, ParseException {


//        ProjectUtility.debug(DataSourceDB.query("show tables"));

//        DBMigration.migrate();
////        DataSourceDB.dropTableAll();
//
////        ProjectUtility.debug(DataSourceDB.load("Users"));
//
//        User user = new User("test", 20);
//        ProjectUtility.debug(user.toString());
//
//        Users.addData(user);
//        ProjectUtility.debug(Users.getData().toString());
//        user.save();
//        user = new User("test2", 20);
//        Users.addData(user);
//        user.save();
//
////        Users.delete(user);
//        ProjectUtility.debug(Users.getData().toString());
//
//        Date date = new Date(System.currentTimeMillis());

//        ProjectUtility.debug(ProjectUtility.getDate("2021-01-01"));
//
//        ProjectUtility.debug(Date.valueOf(LocalDate.now()));

//        DBMigration.migrate(true);
//        DBSeedAndLoad.seed();
        DBSeedAndLoad.quickLoad();
    }
}
