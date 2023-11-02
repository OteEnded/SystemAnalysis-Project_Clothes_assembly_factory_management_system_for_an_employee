package ku.cs.controller.employer;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import ku.cs.entity.Materials;
import ku.cs.model.Material;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;

public class MaterialManagementPageController {

    @FXML private ListView<HBox> materialListview;

    @FXML void initialize() throws SQLException {
        for (Material material : Materials.getDataAsList()){
            materialListview.getItems().add(createMaterialList(material.getName(), material.getUnitName()));
        }
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
        deleteBtn.setOnAction(e -> {
            try {
                handleDeleteMaterialButton(name);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button editBtn = new Button();
        editBtn.setText("แก้ไขวัตถุดิบ");
        editBtn.getStyleClass().add("white-btn");
        editBtn.setOnAction(e -> handleEditMaterialButton(name));

        box.getChildren().add(material);
        box.getChildren().add(editBtn);
        box.getChildren().add(deleteBtn);
        box.setSpacing(20);
        return box;
    }

    private void handleDeleteMaterialButton(String name) throws SQLException {
        ProjectUtility.debug("MaterialManagementPageController[handleDeleteMaterialButton]: trying to delete material ->", getMaterialFromStringName(name));
    }

    private void handleEditMaterialButton(String name){
        System.out.println("edit " + name);
    }

    private Material getMaterialFromStringName(String name) throws SQLException {
        Materials.addFilter("material_name", name);
        return Materials.toList(Materials.getFilteredData()).get(0);
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
            com.github.saacsos.FXRouter.goTo("order",null);
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
