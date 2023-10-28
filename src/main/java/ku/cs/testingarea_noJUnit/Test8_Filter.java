package ku.cs.testingarea_noJUnit;

import ku.cs.entity.Works;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;

public class Test8_Filter {
    public static void main(String[] args) throws SQLException {
        Works.load();
        Works.addFilter("work_type", Works.type_normal);
        ProjectUtility.debug(Works.getFilteredData());
    }
}
