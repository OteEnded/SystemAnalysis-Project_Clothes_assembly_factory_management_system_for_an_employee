package ku.cs.controller.employer;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import ku.cs.entity.MaterialUsages;
import ku.cs.entity.Materials;
import ku.cs.entity.Products;
import ku.cs.model.Material;
import ku.cs.model.MaterialUsage;
import ku.cs.model.Product;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddProductPageController {

    @FXML private ListView<HBox> materialListView;

    @FXML private ComboBox<String> materialNameComboBox;

    @FXML private TextField productTextField;

    @FXML private TextField sizeTextField;

    @FXML private TextField amountTextField;

    @FXML private TextField yieldTextField;
    @FXML private Text unitText;

    @FXML private Label promptLabel;


    @FXML void initialize() throws SQLException {
        refreshMaterialComboBox();
        unitText.setText("");
        yieldTextField.setText("1");
    }

    private void refreshMaterialComboBox() throws SQLException {
        materialNameComboBox.getItems().clear();
        for (Material materials : Materials.getDataAsList()){
            materialNameComboBox.getItems().addAll(materials.getName());
        }
    }

    @FXML public void handleComboBoxSelected(){
        String setStr = "";
        if (materialNameComboBox.getValue() != null) setStr += handleMaterialStringToMaterialObject().getUnitName();
        unitText.setText(setStr);
    }

    private Material handleMaterialStringToMaterialObject(){
        if (materialNameComboBox.getValue() == null) return null;
        Materials.addFilter("material_name", materialNameComboBox.getValue());
        HashMap<String, Object> filter = Materials.getFilter();
        try {
            ProjectUtility.debug("AddProductPageController[handleMaterialStringToMaterialObject]: getting materials filter by material_name ->", materialNameComboBox.getValue());
            Materials.getFilteredData(filter);
            if (Materials.getFilteredData(filter).isEmpty()) ProjectUtility.debug("AddProductPageController[handleMaterialStringToMaterialObject]: cannot fine material with filter by material_name ->", materialNameComboBox.getValue());
            ProjectUtility.debug("AddProductPageController[handleMaterialStringToMaterialObject]: list of filtered material -> ", Materials.toList(Materials.getFilteredData(filter)));

            ProjectUtility.debug("materialNameComboBox.getValue():", materialNameComboBox.getValue());
            ProjectUtility.debug("Materials.getFilteredData(filter):", Materials.getFilteredData(filter));
            ProjectUtility.debug("Materials.toList(Materials.getFilteredData(filter)):", Materials.toList(Materials.getFilteredData(filter)));
            ProjectUtility.debug("Materials.toList(Materials.getFilteredData(filter)).get(0):", Materials.toList(Materials.getFilteredData(filter)).get(0));

            return Materials.toList(Materials.getFilteredData(filter)).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Material handleMaterialStringToMaterialObject(String values){
        Materials.addFilter("material_name", values);
        HashMap<String, Object> filter = Materials.getFilter();
        try {
            ProjectUtility.debug("AddProductPageController[handleMaterialStringToMaterialObject]: getting materials filter by material_name ->", values);
            if (Materials.getFilteredData().isEmpty()) ProjectUtility.debug("AddProductPageController[handleMaterialStringToMaterialObject]: cannot fine material with filter by material_name ->", values);
            ProjectUtility.debug("AddProductPageController[handleMaterialStringToMaterialObject]: list of filtered material -> ", Materials.toList(Materials.getFilteredData(filter)));

            return Materials.toList(Materials.getFilteredData(filter)).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public HBox createMaterialList(String name, int amount, int yield){
        HBox box = new HBox();
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(10);
        Label label = new Label();
        label.setText(name + " (" + amount + " ชิ้น ต่อสินค้า " + yield + " ตัว)");
        Button btn = new Button();
        btn.setOnAction(e -> removeMaterialList(name));
        btn.setText("นำออกจากรายการ");
        btn.getStyleClass().add("white-red-btn");
        box.getChildren().add(label);
        box.getChildren().add(btn);
        box.setSpacing(20);
        return box;
    }

    public void removeMaterialList(String name) {
        try {
            for(HBox box : materialListView.getItems()){
                String materialName =  ((Label) box.getChildren().get(0)).getText().split(" \\(")[0];
                if(name.startsWith(materialName)){
                    ProjectUtility.debug("AddProductPageController[removeMaterialList]: removing material use list for materialName ->", materialName);
                    materialListView.getItems().remove(box);
                }
            }
            materialListView.refresh();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddMaterialToProductButton(){
        String materialName = materialNameComboBox.getValue();

        Button deleteAlreadyAddedButton = null;
        for (HBox hbox: materialListView.getItems()){
            String material_name = ((Label) hbox.getChildren().get(0)).getText().split(" \\(")[0];
            if (materialName.equals(material_name)) {
                deleteAlreadyAddedButton = (Button) hbox.getChildren().get(1);
                break;
            }
        }
        if (deleteAlreadyAddedButton != null) deleteAlreadyAddedButton.fire();

        int amount = Integer.parseInt(amountTextField.getText());
        int yield = Integer.parseInt(yieldTextField.getText());
        HBox box = createMaterialList(materialName, amount, yield);
        materialListView.getItems().add(box);
        materialListView.refresh();

        materialNameComboBox.getSelectionModel().clearSelection();
        amountTextField.setText("");
    }

    private boolean validate() throws SQLException {
        if (productTextField.getText().isEmpty()){
            promptLabel.setText("กรุณากรอกชื่อสินค้า");
            return false;
        }
        if (sizeTextField.getText().isEmpty()){
            promptLabel.setText("กรุณากรอกขนาดสินค้า");
            return false;
        }
        // check if sizeTextField is number
        if (!sizeTextField.getText().matches("[0-9]+")){
            promptLabel.setText("กรุณากรอกขนาดสินค้าเป็นตัวเลข");
            return false;
        }

        Products.addFilter("product_name", productTextField.getText());
        Products.addFilter("size", Integer.parseInt(sizeTextField.getText()));
        if (!Products.getFilteredData().isEmpty()){
            promptLabel.setText("มีสินค้าชื่อ " + productTextField.getText() + " ขนาด " + sizeTextField.getText() + " นิ้ว ในระบบอยู่แล้ว");
            return false;
        }


        return true;

    }

    @FXML
    public void handleAddProductButton() throws SQLException, ParseException {
        try {
            if (!validate()) return;
            promptLabel.setText("");
            Product product = new Product();
            product.setName(productTextField.getText());
            product.setSize(Integer.parseInt(sizeTextField.getText()));
            product.save();

            MaterialUsage materialUsage = new MaterialUsage();
            materialUsage.setProduct(product);

            for (String stringFromHBoxListview: getSelectedMaterialUseListFromHBoxListView()) { // HBox hbox: materialListView.getItems()){
                String material_name = stringFromHBoxListview.split(" \\(")[0];

                String amountStr = stringFromHBoxListview.split(" \\(")[1];
                amountStr = amountStr.split(" ชิ้น")[0];

                String yieldStr = stringFromHBoxListview.split("ต่อสินค้า ")[1];
                yieldStr = yieldStr.split(" ตัว")[0];

                ProjectUtility.debug("AddProductPageController[handleSubmitButton]: adding material usage for product ->", product);
                ProjectUtility.debug("AddProductPageController[handleSubmitButton]: with material, amount, yield ->", material_name, amountStr, yieldStr);

                product.saveMaterialUsed(handleMaterialStringToMaterialObject(material_name), Integer.parseInt(amountStr), Integer.parseInt(yieldStr));
            }


            com.github.saacsos.FXRouter.goTo("product-manage");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }

    private List<String> getSelectedMaterialUseListFromHBoxListView(){
        List<String> selectedMaterialUseHBoxList = new ArrayList<>();
        for (HBox hbox: materialListView.getItems()){
            selectedMaterialUseHBoxList.add(((Label) hbox.getChildren().get(0)).getText());
        }

        return selectedMaterialUseHBoxList;
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

        refreshMaterialComboBox();
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
