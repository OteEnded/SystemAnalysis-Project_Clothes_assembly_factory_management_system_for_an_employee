package ku.cs.testingarea_codeexample;

import ku.cs.entity.Works;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class Test1_NoName {
    public static void main(String[] args) throws SQLException, ParseException {
//        ProjectUtility.debug(DataSourceDB.getTableList());
//        Works.addFilter("status", Works.status_waitForMaterial);
//        Works.addFilter("work_type", Works.type_normal);
//        Works.addFilter("deadline", ProjectUtility.getDate(0));
//        ProjectUtility.debug(Works.getFilteredData());
//        Works.load();


        ProjectUtility.debug(Works.getSqlTable().getColumnsValues("work_id"));

//        Product product = new Product();
//        product.load(1);
//        Work work = new Work();
//        work.setProduct(product);
//        work.setWorkType(Works.type_normal);
//        work.setDeadline(ProjectUtility.getDate(5));
//        work.setGoalAmount(100);
//        work.save();
    }
}
