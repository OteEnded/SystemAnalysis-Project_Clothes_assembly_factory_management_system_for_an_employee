package ku.cs.tool;

import ku.cs.service.DBMigration;
import ku.cs.service.DBSeedAndLoad;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class DBControlPanel {
    public static void main(String[] args) throws SQLException, ParseException {
//        ProjectUtility.setDBSource(ProjectUtility.DB_Azure);
        DBMigration.migrate(true);
        DBSeedAndLoad.seed();
    }
}
