package ku.cs.testingarea_codeexample;

import ku.cs.entity.DailyRecords;
import ku.cs.entity.Materials;
import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;

public class Example7_sorting {
    public static void main(String[] args) throws SQLException {
        Products.addFilter("product_id", "P00001");
        ProjectUtility.debug(Products.getFilteredData());
        Products.addFilter("product_id", "P00001");
        ProjectUtility.debug(Products.getSortedBy("product_id", Products.getFilteredData()));
//
//        Works.addFilter("work_id", "W00001");
//        ProjectUtility.debug(Works.getFilteredData());
//        ProjectUtility.debug(Works.getSortedBy("work_id", Works.getData()));
//
//        Materials.addFilter("material_id", "M00001");
//        ProjectUtility.debug(Materials.getFilteredData());
//        ProjectUtility.debug(Materials.getSortedBy("material_id", Materials.getData()));
//
//        DailyRecords.addFilter("for_work", "W00001");
//        ProjectUtility.debug(DailyRecords.getFilteredData());
//        ProjectUtility.debug(DailyRecords.getSortedBy("date", DailyRecords.getData()));
    }
}
