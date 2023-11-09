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
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EditProductPageController {

    @FXML private ListView<HBox> materialListView;

    @FXML private ComboBox<String> materialNameComboBox;

    @FXML private TextField productTextField;

    @FXML private TextField sizeTextField;

    @FXML private TextField amountTextField;

    @FXML private TextField yieldTextField;
    @FXML private Text unitText;

    @FXML private Label promptLabel;


    @FXML void initialize() throws SQLException {
        Product product = (Product) com.github.saacsos.FXRouter.getData();
        productTextField.setText(product.getName());
        sizeTextField.setText(String.valueOf(product.getSize()));
        for (Material materials : Materials.getDataAsList()){
            materialNameComboBox.getItems().addAll(materials.getName());
        }
        unitText.setText("");
        yieldTextField.setText("1");
        initMaterialList(product);
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
            if (Materials.getFilteredData().isEmpty()) ProjectUtility.debug("EditProductPageController[handleMaterialStringToMaterialObject]: cannot fine material with filter by material_name ->", materialNameComboBox.getValue());
            ProjectUtility.debug("EditProductPageController[handleMaterialStringToMaterialObject]: list of filtered material -> ", Materials.toList(Materials.getFilteredData(filter)));
            return Materials.toList(Materials.getFilteredData(filter)).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Material handleMaterialStringToMaterialObject(String values){
        Materials.addFilter("material_name", values);
        HashMap<String, Object> filter = Materials.getFilter();
        try {
//            System.out.println(Materials.toList(Materials.getFilteredData(filter)));
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

    private void initMaterialList(Product product) throws SQLException {
        for (MaterialUsage materialUsage : product.getMaterialsUsed()){
            HBox box = createMaterialList(materialUsage.getMaterial().getName(), materialUsage.getAmount(), materialUsage.getYield());
            materialListView.getItems().add(box);
            Collections.reverse(materialListView.getItems());
            materialListView.refresh();
        }
    }

    public void removeMaterialList(String name) {
        try {
            for(HBox box : materialListView.getItems()){
                String materialName =  ((Label) box.getChildren().get(0)).getText().split(" \\(")[0];
                if(name.startsWith(materialName)){
                    ProjectUtility.debug("EditProductPageController[removeMaterialList]: removing material use list for materialName ->", materialName);
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
    public void handleAddMaterialToProductButton() throws SQLException {
        String materialName = materialNameComboBox.getValue();
        if(!validateAddMaterial()) return;
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
        Collections.reverse(materialListView.getItems());
        materialListView.refresh();

        materialNameComboBox.getSelectionModel().clearSelection();
        amountTextField.setText("");
    }

    private boolean validate(){
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
        // check if sizeTextField is integer (less than long)
        if (Long.parseLong(sizeTextField.getText()) > Integer.MAX_VALUE){
            promptLabel.setText("กรุณากรอกขนาดสินค้าเป็นตัวเลข (out of bound of Integer)");
            return false;
        }
        // check if sizeTextField is in range 0-199
        if (Integer.parseInt(sizeTextField.getText()) <= 0 || Integer.parseInt(sizeTextField.getText())  > 200 ){
            promptLabel.setText("กรุณากรอกขนาดสินค้าให้ถูกต้อง (out of bound range)");
            return false;
        }
        // check if material is null
        if (materialListView.getItems().isEmpty()){
            promptLabel.setText("กรุณาใส่วัตถุดิบที่ใช้ในการผลิต");
            return false;
        }

        return true;
    }

    private boolean validateAddMaterial() throws SQLException {
        if(materialNameComboBox.getValue() == null){
            promptLabel.setText("กรุณากรอกชื่อวัตถุดิบ");
            return false;
        }
        if(!amountTextField.getText().matches("[0-9]+")){
            promptLabel.setText("กรุณากรอกจำนวนของวัตถุดิบเป็นตัวเลข");
            return false;
        }
        if(Long.parseLong(amountTextField.getText()) > Integer.MAX_VALUE){
            promptLabel.setText("กรุณากรอกจำนวนของวัตถุดิบให้ถูกต้อง (out of bound of Integer)");
            return false;
        }
        if(!yieldTextField.getText().matches("[0-9]+")){
            promptLabel.setText("กรอกจำนวนสินค้าต่อวัตถุดิบเป็นตัวเลข");
            return false;
        }
        if(Long.parseLong(amountTextField.getText()) > Integer.MAX_VALUE){
            promptLabel.setText("กรอกจำนวนสินค้าต่อวัตถุดิบให้ถูกต้อง (out of bound of Integer)");
            return false;
        }

        return true;
    }

    @FXML
    public void handleSubmitButton() throws SQLException, ParseException {
        try {
            if (!validate()) return;
            promptLabel.setText("");
            Product product = (Product) com.github.saacsos.FXRouter.getData();
            product.setName(productTextField.getText().replaceAll("\\s","")); //cut whitespace in string
            product.setSize(Integer.parseInt(sizeTextField.getText()));

            List<String> deletingMaterialUsageList = new ArrayList<>();
            for (MaterialUsage materialUsage: product.getMaterialsUsed()) deletingMaterialUsageList.add(MaterialUsages.getJoinedPrimaryKeys(materialUsage));

            for (String stringFromHBoxListview: getSelectedMaterialUseListFromHBoxListView()) { // HBox hbox: materialListView.getItems()){
                String material_name = stringFromHBoxListview.split(" \\(")[0];

                String amountStr = stringFromHBoxListview.split(" \\(")[1];
                amountStr = amountStr.split(" ชิ้น")[0];

                String yieldStr = stringFromHBoxListview.split("ต่อสินค้า ")[1];
                yieldStr = yieldStr.split(" ตัว")[0];

                ProjectUtility.debug("EditProductPageController[handleSubmitButton]: adding material usage for product ->", product);
                ProjectUtility.debug("EditProductPageController[handleSubmitButton]: with material, amount, yield ->", material_name, amountStr, yieldStr);

                product.saveMaterialUsed(handleMaterialStringToMaterialObject(material_name), Integer.parseInt(amountStr), Integer.parseInt(yieldStr));
                deletingMaterialUsageList.remove(String.join("|", product.getId(), handleMaterialStringToMaterialObject(material_name).getId()));
            }

            ProjectUtility.debug("EditProductPageController[handleSubmitButton]: deletingMaterialUsageList ->", deletingMaterialUsageList);

            for (String primaryKey: deletingMaterialUsageList){
                MaterialUsages.getData().get(primaryKey).delete();
            }

            product.save();
            com.github.saacsos.FXRouter.goTo("product-manage");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }

    private List<String> getSelectedMaterialUseListFromHBoxListView(){
        List<String> selectedMaterialUseHBoxList = new ArrayList<>();
        for (HBox hbox: materialListView.getItems()) selectedMaterialUseHBoxList.add(((Label) hbox.getChildren().get(0)).getText());

        return selectedMaterialUseHBoxList;
    }

    @FXML
    public void handleAddMaterialButton(){
        // popup
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
