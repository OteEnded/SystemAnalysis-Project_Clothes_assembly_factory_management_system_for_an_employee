package ku.cs.testingarea_codeexample;

import ku.cs.entity.Works;
import ku.cs.model.Work;
import ku.cs.service.WorkCalendar;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class Example8_WorkCalendar {
    public static void main(String[] args) throws SQLException, ParseException {

//        WorkCalendar.init();

//        for (Work work: Works.getDataAsList()){
//            ProjectUtility.debug(work.getEstimated());
//        }
//
//        Work work = new Work();
//        work.setDeadline(ProjectUtility.getDate(5));
//        work.setProduct("P00001");
//        work.setGoalAmount(1);
//
//        ProjectUtility.debug(work);
//        ProjectUtility.debug(work.getEstimated());

        Works.addFilter("status", Works.status_waitForAccept);
        ProjectUtility.debug(Works.getFilteredData());



        Work work = new Work();
        work.load(4);
        ProjectUtility.debug(work.getEstimated());
        ProjectUtility.debug(work.getRecommendedProgressRate());
    }
}
