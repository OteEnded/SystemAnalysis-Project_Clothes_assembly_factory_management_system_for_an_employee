package ku.cs.testingarea_noJUnit;

import ku.cs.entity.Works;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class Test5_works {
    public static void main(String[] args) throws SQLException, ParseException {
//        ProjectUtility.debug(ProjectUtility.getDate());
//        Works.load();
//        ProjectUtility.debug(Works.getData());
//        Product product = new Product();
//        product.load(1);
//        ProjectUtility.debug(product.getData());
        Work work = new Work();
        ProjectUtility.debug(work);
        ProjectUtility.debug(Works.verifyWork(work));
        work.save();
//        work.setWorkType(Works.type_normal);
//        work.setProduct(product);
//        work.setDeadline(ProjectUtility.getDate());
//        work.setCreateDate(ProjectUtility.getDate());
//        work.setGoalAmount(100);
//        work.setProgressAmount(0);
//        work.setStatus(Works.status_waitForAccept);
//        work.save();
    }
}
