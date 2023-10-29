package ku.cs.controller.employer;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class MaterialManagementPageController {

    @FXML private ListView<HBox> materialListview;

    @FXML void initialize() {
        HBox list1 = createMaterialList("ผ้ายจากคนดำ", "คน");
        HBox list2 = createMaterialList("ผ้ายจากคนดำ", "คน");
        HBox list3 = createMaterialList("ผ้ายจากคนดำ", "คน");
        HBox list4 = createMaterialList("ผ้ายจากคนดำ", "คน");
        HBox list5 = createMaterialList("ผ้ายจากคนดำ", "คน");
        materialListview.getItems().add(list1);
        materialListview.getItems().add(list2);
        materialListview.getItems().add(list3);
        materialListview.getItems().add(list4);
        materialListview.getItems().add(list5);
    }

    public HBox createMaterialList(String name, String unit){
        HBox box = new HBox();
        box.setPadding(new Insets(10, 10, 10, 10));
        Label material = new Label();
        material.setText(name + " " + "[หน่วย : " + unit + "]");
        material.setPrefWidth(500);

        Button deleteBtn = new Button();
        deleteBtn.setText("ลบวัตถุดิบ");
        deleteBtn.getStyleClass().add("white-red-btn");

        Button editBtn = new Button();
        editBtn.setText("แก้ไขวัตถุดิบ");
        editBtn.getStyleClass().add("white-btn");

        box.getChildren().add(material);
        box.getChildren().add(editBtn);
        box.getChildren().add(deleteBtn);
        box.setSpacing(20);
        return box;
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
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }
    @FXML
    public void handleProductManagementButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("product-manage");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
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
