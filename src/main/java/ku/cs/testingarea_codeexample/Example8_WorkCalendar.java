package ku.cs.testingarea_codeexample;

import ku.cs.entity.Works;
import ku.cs.model.Work;
import ku.cs.service.WorkCalendar;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;

public class Example8_WorkCalendar {
    public static void main(String[] args) throws SQLException {

//        WorkCalendar.init();

        for (Work work: Works.getDataAsList()){
            ProjectUtility.debug(work.getEstimated());
        }

        Work work = new Work();
        work.setDeadline(ProjectUtility.getDate(5));
        work.setProduct("P00001");
        work.setGoalAmount(1);

        ProjectUtility.debug(work);
        ProjectUtility.debug(work.getEstimated());
    }
}
