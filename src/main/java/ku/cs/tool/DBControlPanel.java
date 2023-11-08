package ku.cs.tool;

import ku.cs.service.DBMigration;
import ku.cs.service.DBSeedAndLoad;

import java.sql.SQLException;
import java.text.ParseException;

public class DBControlPanel {
    public static void main(String[] args) throws SQLException, ParseException {
        DBMigration.migrate(true);
//        DBSeedAndLoad.seed();
    }
}
