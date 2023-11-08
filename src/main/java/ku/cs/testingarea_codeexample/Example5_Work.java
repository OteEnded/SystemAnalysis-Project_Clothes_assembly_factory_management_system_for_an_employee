package ku.cs.testingarea_codeexample;

import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.model.Work;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class Example5_Work {
    public static void main(String[] args) throws SQLException, ParseException {
//        Work w4 = new Work();
//        w4.getAll("W00004");
//        ProjectUtility.debug(w4.getData());
//
//        ProjectUtility.debug(w4.isPass());
//        ProjectUtility.debug(w4.getRepairWork());
//
//
//        Work w1 = new Work();
//        w1.getAll("W00001");
//        ProjectUtility.debug(w1.getData());
//
//        ProjectUtility.debug(w1.isPass());
//        ProjectUtility.debug(w1.getRepairWork());
//        Work work = new Work();
//        work.getAll("W00005");
//        ProjectUtility.debug(work.getEstimated());
//
//        Work work2 = new Work();
//        work2.setWorkType(Works.type_normal);
//        work2.setProduct("P00001");
//        work2.setDeadline(ProjectUtility.getDate(2025, 5, 1));
//        work2.setGoalAmount(1);
//        work2.setStartDate(ProjectUtility.getDate());
//        work2.setProgressAmount(0);
//        work2.setCreateDate(ProjectUtility.getDate());
//        work2.setStatus(Works.status_working);
//        work2.save();
//
//        ProjectUtility.debug(work2.getEstimated());

//        ProjectUtility.debug(Works.getAbnormalWorks());
//        Works.load();
//        Works.addFilter("status", Works.status_done);
//        ProjectUtility.debug(Works.getFilteredData());

        Work work = new Work();
        work.load("W00001");
        ProjectUtility.debug(work);
    }
}
