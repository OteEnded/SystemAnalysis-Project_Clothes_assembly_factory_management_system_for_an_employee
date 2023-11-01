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

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class AddProductPageController {

    @FXML private ListView<HBox> materialListView;

    @FXML private ComboBox<String> materialNameComboBox;

    @FXML private TextField productTextField;

    @FXML private TextField sizeTextField;

    @FXML private TextField amountTextField;

    @FXML private TextField yieldTextField;
    @FXML private Text unitText;


    @FXML void initialize() throws SQLException {
        for (Material materials : Materials.getDataAsList()){
            materialNameComboBox.getItems().addAll(materials.getName());
        }
        unitText.setText("");
        yieldTextField.setText("1");
    }

    @FXML public void handleComboBoxSelected(){
        unitText.setText(handleMaterialStringToMaterialObject().getUnitName());
    }

    private Material handleMaterialStringToMaterialObject(){
        Materials.addFilter("material_name", materialNameComboBox.getValue());
        try {
            return Materials.toList(Materials.getFilteredData()).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Material handleMaterialStringToMaterialObject(String values){
        Materials.addFilter("material_name", values);
        HashMap<String, Object> filter = Materials.getFilter();
        try {
            System.out.println(Materials.toList(Materials.getFilteredData(filter)));
            return Materials.toList(Materials.getFilteredData(filter)).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public HBox createMaterialList(String name, int amount){
        HBox box = new HBox();
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(10);
        Label label = new Label();
        label.setText(name + "(" + amount + " " + "ชิ้น)");
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
        System.out.println("delete " + name);
        for(HBox box : materialListView.getItems()){
            String matName =  ((Label) box.getChildren().get(0)).getText().split("\\(")[0];
            if(matName.equals(name)){
                materialListView.getItems().remove(box);
            }
        }
        materialListView.refresh();
    }

    @FXML
    public void handleAddMaterialToProductButton(){
        String materialName = materialNameComboBox.getValue();
        int amount = Integer.parseInt(amountTextField.getText());
        HBox box = createMaterialList(materialName, amount);
        materialListView.getItems().add(box);
        materialListView.refresh();
    }
    @FXML
    public void handleAddProductButton() throws SQLException, ParseException {
        Product product = new Product();
        product.setName(productTextField.getText());
        product.setSize(Integer.parseInt(sizeTextField.getText()));
        product.save();

        MaterialUsage materialUsage = new MaterialUsage();
        materialUsage.setProduct(product);

        for (HBox hbox: materialListView.getItems()){
            String material_name = ((Label) hbox.getChildren().get(0)).getText().split("\\(")[0];
            product.saveMaterialUsed(handleMaterialStringToMaterialObject(material_name), Integer.parseInt(amountTextField.getText()), Integer.parseInt(yieldTextField.getText()));
        }
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
