package ku.cs.testingarea_noJUnit;

import ku.cs.entity.Users;
import ku.cs.model.User;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class Test3_fixThings {
    public static void main(String[] args) throws SQLException, ParseException {
//        User user = new User();
//        user.load(1);
//        ProjectUtility.debug(user);
//
//        user.setAge(33);
//        user.save();
//        ProjectUtility.debug(user);
//
//        user = new User("Ote", 21);
//        user.save();
//        ProjectUtility.debug(user);
//        ProjectUtility.debug(Users.getData());

        User user = new User();
        user.load(3);
        ProjectUtility.debug(user);
        user.delete();
        ProjectUtility.debug(Users.getData());
    }
}
