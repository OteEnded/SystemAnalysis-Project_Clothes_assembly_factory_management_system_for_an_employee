package ku.cs.controller.employer;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class AddProductPageController {

    @FXML private ListView<HBox> materialListView;

    @FXML private ComboBox<String> materialNameComboBox;

    @FXML private TextField amountTextField;

    @FXML private TextField yieldTextField;
    @FXML private Text unitText;


    @FXML void initialize() {
        // จำลองข้อมูลใน combo box
        materialNameComboBox.getItems().addAll("ผ้าไหม", "ผ้าฝ้าย");
        unitText.setText("");
        yieldTextField.setText("1");
    }

    @FXML public void handleComboBoxSelected(){
        unitText.setText("ชิ้น");
    }



    public HBox createMaterialList(String name, int amount){
        HBox box = new HBox();
        box.setPadding(new Insets(10, 10, 10, 10));
        Label label = new Label();
        label.setText(name + " " + amount + " " + "ชิ้น");
        Button btn = new Button();
        btn.setOnMouseClicked(e -> removeMaterialList(name));
        btn.setText("นำออกจากรายการ");
        btn.getStyleClass().add("white-red-btn");
        box.getChildren().add(label);
        box.getChildren().add(btn);
        box.setSpacing(20);
        return box;
    }

    public void removeMaterialList(String name) {
        for(HBox box : materialListView.getItems()){
            String matName = ((Label) box.getChildren().get(0)).getText().split(" ")[0];
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
    public void handleAddProductButton(){

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
