package ku.cs.testingarea_codeexample;

import ku.cs.entity.Works;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;

public class Test1_NoName {
    public static void main(String[] args) throws SQLException {
//        ProjectUtility.debug(DataSourceDB.getTableList());
        ProjectUtility.debug(Works.getSqlTable());
    }
}
