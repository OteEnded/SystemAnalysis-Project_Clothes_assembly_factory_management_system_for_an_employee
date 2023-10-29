package ku.cs.testingarea_noJUnit;

import ku.cs.model.Product;

import java.sql.SQLException;
import java.text.ParseException;

public class Test4_products {
    public static void main(String[] args) throws SQLException, ParseException {
//        Product p1 = new Product("pant", 15);
//        p1.save();
        Product pt = new Product();
        pt.load("P00001");
        System.out.println(pt);
    }
}
