package ku.cs.controller.employer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ku.cs.entity.Materials;
import ku.cs.model.Product;

import java.io.IOException;

public class ProductManagementPageController {
    @FXML private ListView<Product> productListView;
    @FXML private ListView<String> materialListView;
    @FXML private Label productLabel;

    @FXML
    void initialize(){
        
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
