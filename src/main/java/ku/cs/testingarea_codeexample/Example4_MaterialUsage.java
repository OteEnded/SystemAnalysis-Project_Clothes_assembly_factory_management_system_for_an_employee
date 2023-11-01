package ku.cs.testingarea_codeexample;

import ku.cs.entity.MaterialUsages;
import ku.cs.entity.Products;
import ku.cs.model.MaterialUsage;
import ku.cs.model.Product;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.util.List;

public class Example4_MaterialUsage {
    public static void main(String[] args) throws SQLException {
        Product product = new Product();
        product.load(1);

        ProjectUtility.debug(product.getMaterialsUsed());

        MaterialUsages.addFilter("product_id", "P00001");
        ProjectUtility.debug(MaterialUsages.getFilteredData());
        MaterialUsages.addFilter("product_id", "P00001");
        ProjectUtility.debug(MaterialUsages.getSortedBy("material_id", MaterialUsages.getFilteredData()));
    }
}
