package ku.cs.testingarea_codeexample;

import ku.cs.entity.Works;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class Test1_NoName {
    public static void main(String[] args) throws SQLException, ParseException {
//        ProjectUtility.debug(DataSourceDB.getTableList());
        Works.addFilter("status", Works.status_waitForMaterial);
        Works.addFilter("work_type", Works.type_normal);
        Works.addFilter("deadline", ProjectUtility.getDate(0));
        ProjectUtility.debug(Works.getFilteredData());
//        Works.load();
    }
}
