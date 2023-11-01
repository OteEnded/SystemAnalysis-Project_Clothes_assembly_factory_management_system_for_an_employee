package ku.cs.testingarea_codeexample;

import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.model.Work;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.util.HashMap;

public class Example5_Work {
    public static void main(String[] args) throws SQLException {
        Work w4 = new Work();
        w4.load("W00004");
        ProjectUtility.debug(w4.getData());

        ProjectUtility.debug(w4.isPass());
        ProjectUtility.debug(w4.getRepairWork());


        Work w1 = new Work();
        w1.load("W00001");
        ProjectUtility.debug(w1.getData());

        ProjectUtility.debug(w1.isPass());
        ProjectUtility.debug(w1.getRepairWork());

    }
}
