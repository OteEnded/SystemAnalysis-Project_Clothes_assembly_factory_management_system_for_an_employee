package ku.cs.testingarea_noJUnit;

import ku.cs.entity.Users;
import ku.cs.model.User;
import ku.cs.service.DBMigration;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.JdbcConnector;
import ku.cs.utility.ProjectUtility;

import java.sql.*;
import java.text.ParseException;

public class Test1_setup {
    public static void main(String[] args) throws SQLException, IllegalAccessException, ParseException {


        DBMigration.migrate(true);
//        DataSourceDB.dropTableAll();

//        ProjectUtility.debug(DataSourceDB.load("Users"));

        User user = new User("test", 20);
        ProjectUtility.debug(user.toString());

        Users.addData(user);
        user.save();
//        user = new User("test2", 20);
//        Users.addData(user);
//        user.save();

        Users.delete(user);
        ProjectUtility.debug(Users.getData().toString());

        Date date = new Date(System.currentTimeMillis());
    }
}
