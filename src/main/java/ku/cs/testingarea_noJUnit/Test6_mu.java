package ku.cs.testingarea_noJUnit;

import ku.cs.entity.MaterialUsages;
import ku.cs.entity.Works;
import ku.cs.model.Material;
import ku.cs.model.MaterialUsage;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class Test6_mu {
    public static void main(String[] args) throws SQLException, ParseException {

//        Material material = new Material();
//        material.setName("ผ้าขาว");
//        material.setUnit("เมตร");
//        material.save();

        MaterialUsages.load();
        ProjectUtility.debug("MaterialUsages.getData()", MaterialUsages.getData());
        MaterialUsage materialUsage = new MaterialUsage();
        Product product = new Product();
        product.load(1);
        materialUsage.setProduct(product);
        Material material = new Material();
        material.load(1);
        materialUsage.setMaterial(material);
        materialUsage.setAmount(10);
        materialUsage.setYield(1);
        ProjectUtility.debug("materialUsage", materialUsage);
        materialUsage.save();
        ProjectUtility.debug("MaterialUsages.getData()", MaterialUsages.getData());
    }
}
