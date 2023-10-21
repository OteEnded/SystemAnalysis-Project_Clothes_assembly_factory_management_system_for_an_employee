package ku.cs.testingarea_noJUnit;

import ku.cs.entity.Users;
import ku.cs.model.User;
import ku.cs.service.DBMigration;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.JdbcConnector;
import ku.cs.utility.ProjectUtility;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class Test1_setup {
    public static void main(String[] args) throws SQLException, IllegalAccessException, ParseException {
        DBMigration.migrate(true);

        ProjectUtility.debug(DataSourceDB.load("Users"));

        User user = new User("test", 20);
        ProjectUtility.debug(user.toString());

        Users.addData(user);
        user.save();
        user.setName("Ote");
        user.save();
        ProjectUtility.debug(Users.getData().toString());
    }
}
