package ku.cs.testingarea_noJUnit;

import ku.cs.model.Material;
import ku.cs.model.Product;

import java.sql.SQLException;
import java.text.ParseException;

public class Test4_products {
    public static void main(String[] args) throws SQLException, ParseException {
//        Product p1 = new Product("pant", 15);
//        p1.save();
//        Product pt = new Product();
//        pt.load("P00001");
//        pt.setName("shirt");
//        System.out.println(pt);
//        pt.setName("pant");
//        pt.save();
//        pt.load("P00001");
//        System.out.println(pt);
//        Product pn = new Product();
//        pn.setName("newShirt");
//        pn.save();
//        System.out.println(pn);

        Material material = new Material();
        material.load(1);

        Product product = new Product();
        product.load(1);
        product.deleteMaterialUsed(material);
        product.save();
    }
}
