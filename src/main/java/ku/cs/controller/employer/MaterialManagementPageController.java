package ku.cs.controller.employer;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import ku.cs.entity.Materials;
import ku.cs.model.Material;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class MaterialManagementPageController {

    @FXML private ListView<HBox> materialListview;

    @FXML void initialize() throws SQLException {
        refreshMaterialList();
    }

    private void refreshMaterialList() throws SQLException {
        materialListview.getItems().clear();
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
            } catch (SQLException | IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button editBtn = new Button();
        editBtn.setText("แก้ไขวัตถุดิบ");
        editBtn.getStyleClass().add("white-btn");
        editBtn.setOnAction(e -> {
            try {
                handleEditMaterialButton(name);
            } catch (SQLException | IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        });

        box.getChildren().add(material);
        box.getChildren().add(editBtn);
        box.getChildren().add(deleteBtn);
        box.setSpacing(20);
        return box;
    }

    private void handleDeleteMaterialButton(String name) throws SQLException, IOException, ParseException {
        ProjectUtility.debug("MaterialManagementPageController[handleDeleteMaterialButton]: trying to delete material ->", getMaterialFromStringName(name));

        HashMap<String, Object> passingData = new HashMap<>();
        passingData.put("windowsTitle", "ลบวัตถุดิบ");
        passingData.put("headerLabel", "คุณต้องการลบวัตถุดิบหรือไม่");
        passingData.put("objectLabel", name);
        PopUpUtility.popUp("delete-confirmation", passingData);
        if (!PopUpUtility.getPopUp("delete-confirmation").isPositiveClosing()) return;
        PopUpUtility.getPopUp("delete-confirmation").clearData();

        getMaterialFromStringName(name).delete();

        refreshMaterialList();
    }

    private void handleEditMaterialButton(String name) throws SQLException, IOException, ParseException {
        Material editingMaterial = getMaterialFromStringName(name);
        if (editingMaterial == null) throw new RuntimeException("MaterialManagementPageController[handleEditMaterialButton]: editingMaterial is null");

        HashMap<String, Object> passingData = new HashMap<>();
        passingData.put("windowsTitle", "แก้ไขวัตถุดิบ");
        passingData.put("material", editingMaterial);
        PopUpUtility.popUp("save-material", passingData);
        if (!PopUpUtility.getPopUp("save-material").isPositiveClosing()) return;

        editingMaterial = (Material) PopUpUtility.getPopUp("save-material").getPassingData();
        ProjectUtility.debug("MaterialManagementPageController[handleEditMaterialButton]: editingMaterial ->", editingMaterial);

        editingMaterial.save();

        PopUpUtility.getPopUp("save-material").clearData();

        refreshMaterialList();

    }

    private Material getMaterialFromStringName(String name) throws SQLException {
        Materials.addFilter("material_name", name);
        HashMap<String, Object> filter = Materials.getFilter();
        Materials.getFilteredData(filter);
        if (Materials.getFilteredData(filter).isEmpty()) {
            ProjectUtility.debug("MaterialManagementPageController[getMaterialFromStringName]: cannot find material with name ->", name);
            return null;
        }
        return Materials.toList(Materials.getFilteredData(filter)).get(0);
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

    @FXML
    public void handleAddMaterialButton() throws IOException, SQLException, ParseException {
        HashMap<String, Object> passingData = new HashMap<>();
        passingData.put("windowsTitle", "เพิ่มวัตถุดิบ");
        PopUpUtility.popUp("save-material", passingData);
        if (!PopUpUtility.getPopUp("save-material").isPositiveClosing()) return;

        Material addingMaterial = (Material) PopUpUtility.getPopUp("save-material").getPassingData();
        ProjectUtility.debug("MaterialManagementPageController[handleAddMaterialButton]: addingMaterial ->", addingMaterial);

        addingMaterial.save();

        PopUpUtility.getPopUp("save-material").clearData();

        refreshMaterialList();
    }
}
