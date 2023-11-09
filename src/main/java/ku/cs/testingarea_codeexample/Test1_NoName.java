package ku.cs.testingarea_codeexample;

import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class Test1_NoName {
    public static void main(String[] args) throws SQLException, ParseException {
//        Product p1 = new Product();
//        p1.load(1);
//
//        Work w1 = new Work();
//        w1.setWorkType(Works.type_normal);
//        w1.setProduct(p1);
//        w1.setStatus(Works.status_checked);
//        w1.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-09-27
//        w1.setDeadline(ProjectUtility.getDate(10)); // Excel : 2023-09-31
//        w1.setGoalAmount(20);
//        w1.setProgressAmount(20);
//        w1.setNote("-");
//        w1.save();

//        Products.addFilter("product_id", "P00001");
//        ProjectUtility.debug(Products.getFilteredData());

//        ProjectUtility.debug(DataSourceDB.query("show tables"));

//        Product p1 = new Product();
//        p1.load(1);
//
//        ProjectUtility.debug(p1.getProgressRate());


        ProjectUtility.debug();
    }
}
