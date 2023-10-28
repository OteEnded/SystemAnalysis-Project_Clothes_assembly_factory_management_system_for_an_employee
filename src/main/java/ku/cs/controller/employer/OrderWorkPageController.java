package ku.cs.controller.employer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ku.cs.entity.Works;
import ku.cs.model.Product;

import java.io.IOException;
import java.util.Date;

public class OrderWorkPageController {
    @FXML private Text headerText;
    @FXML private ComboBox<String> workTypeComboBox;
    @FXML private ComboBox<Product> productComboBox;
    @FXML private TextField amountTextField;
    @FXML private DatePicker deadlineDatePicker;
    @FXML private TextArea noteTextArea;

    @FXML private Label promptLabel;

    @FXML
    void initialize(){
        workTypeComboBox.getItems().addAll("งานธรรมดา", "งานเร่ง");
        workTypeComboBox.getSelectionModel().select("งานธรรมดา");

        productComboBox.getItems().addAll(new Product(), new Product());

        promptLabel.setText("");
    }

    @FXML
    public void handleSubmitButton() throws IOException{
        if (validate()) {
            promptLabel.setText("");
            System.out.println("บันทึกงานสำเร็จ");

            try {
                com.github.saacsos.FXRouter.goTo("wait-for-receive");
            } catch (Exception e){
                System.err.println("ไปหน้า wait-for-receive ไม่ได้");
                e.printStackTrace();
            }
        }
    }

    private boolean validate(){
        if (amountTextField.getText().isEmpty()){
            promptLabel.setText("กรุณากรอกจำนวนงาน");
            return false;
        }
        return true;
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
