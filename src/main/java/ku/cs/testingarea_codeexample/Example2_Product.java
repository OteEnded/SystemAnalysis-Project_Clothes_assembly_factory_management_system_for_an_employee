package ku.cs.testingarea_codeexample;

import ku.cs.entity.Products;
import ku.cs.model.Product;

import java.sql.SQLException;
import java.util.HashMap;

public class Example2_Product {

    /**
     * you can test your code here
     */
    public static void main(String[] args) throws SQLException {

        // create new HashMap to store all product
        HashMap<String, Product> products = new HashMap<>();
        // use Products.getData() to get all product from buffer
        // this will also getAll data from database to buffer if buffer is empty
        products = Products.getData();

        // create new HashMap to store filtered product
        HashMap<String, Product> filteredProducts = new HashMap<>();
        Products.addFilter("name", "test");

    }
}
