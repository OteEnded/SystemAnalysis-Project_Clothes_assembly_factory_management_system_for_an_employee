package ku.cs.testingarea_noJUnit;

import ku.cs.model.DailyRecord;
import ku.cs.model.Work;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class Test7_dr {
    public static void main(String[] args) throws SQLException, ParseException {
        DailyRecord dailyRecord = new DailyRecord();
        Work work = new Work();
        work.load(1);
        dailyRecord.load(work, ProjectUtility.getDate());
        ProjectUtility.debug(dailyRecord.getForWork());

    }
}
