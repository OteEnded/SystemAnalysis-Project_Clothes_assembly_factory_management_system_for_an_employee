package ku.cs.controller.employer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import ku.cs.entity.MaterialUsages;
import ku.cs.entity.Materials;
import ku.cs.entity.Products;
import ku.cs.model.MaterialUsage;
import ku.cs.model.Product;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductManagementPageController {
    @FXML private ListView<String> productListView;
    @FXML private AnchorPane productDetailPane;
    @FXML private Label productLabel;
    @FXML private ListView<String> materialListView;

    @FXML
    void initialize(){
        productDetailPane.setVisible(false);
        try {
            showListView();
        } catch (SQLException e) {
            System.err.println("แสดง Listview ไม่ได้");
            e.printStackTrace();
        }
        handleSelectedListView();
    }

    private void showListView() throws SQLException {
        for (Product product: Products.getDataAsList()) {
            productListView.getItems().addAll(product.getName() + " "
                    + product.getSize() + " นิ้ว");
        }
    }

    private void handleSelectedListView(){
        productListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        productDetailPane.setVisible(true);
                        productLabel.setText((handleProductStringToProductObject(newValue).getName()));
                        try {
                            showMaterialListView(handleProductStringToProductObject(newValue));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

    }

    private Product handleProductStringToProductObject(String value){
        String[] values = value.split(" ");
        Products.addFilter("product_name", values[0]);
        Products.addFilter("size", Integer.parseInt(values[1]));
        try {
            return Products.toList(Products.getFilteredData()).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showMaterialListView(Product product) throws SQLException {
        materialListView.getItems().clear();
        List<MaterialUsage> materialUsages = product.getMaterialsUsed();
        ProjectUtility.debug(materialUsages);
        for (MaterialUsage materialUsage : materialUsages) {
            materialListView.getItems().add(materialUsage.getMaterial().getName());
        }
    }


    @FXML
    public void handleAddProductButton(){
        try {
            com.github.saacsos.FXRouter.goTo("add-product");
        } catch (Exception e){
            System.err.println("ไปหน้า add product ไม่ได้");
            e.printStackTrace();
        }
    }

    // MenuBar Handle
    @FXML
    public void handleLogoutButton() throws IOException {
        try {
            com.github.saacsos.FXRouter.goTo("home");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }
    @FXML
    public void handleOrderWorkButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("order");
        } catch (Exception e){
            System.err.println("ไปหน้า order ไม่ได้");
            e.printStackTrace();
        }
    }
    @FXML
    public void handleMaterialManagementButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("material-manage");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleWorkManagementButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("wait-for-receive");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }
}
