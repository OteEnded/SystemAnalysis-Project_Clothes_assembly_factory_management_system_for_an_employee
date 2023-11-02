package ku.cs.controller.employer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class OrderWorkPageController {
    @FXML private Text headerText;
    @FXML private ComboBox<String> workTypeComboBox;
    @FXML private ComboBox<String> productComboBox;
    @FXML private TextField amountTextField;
    @FXML private DatePicker deadlineDatePicker;
    @FXML private TextArea noteTextArea;

    @FXML private Label promptLabel;

    @FXML
    void initialize() throws SQLException {
        workTypeComboBox.getItems().addAll(Works.type_normal, Works.type_rush);
        workTypeComboBox.getSelectionModel().select(Works.type_normal);
        for (Product product : Products.getDataAsList()){
            productComboBox.getItems().addAll(product.getName() + " "
                                                + product.getSize() + " นิ้ว");
        }
        promptLabel.setText("");
    }

    @FXML
    public void handleSubmitButton() throws IOException{
        if (validate()) {
            promptLabel.setText("");
            addWork();

            try {
                com.github.saacsos.FXRouter.goTo("wait-for-receive");
            } catch (Exception e){
                System.err.println("ไปหน้า wait-for-receive ไม่ได้");
                e.printStackTrace();
            }
        }
    }

    private void addWork(){
        Work work = new Work();
        work.setWorkType(workTypeComboBox.getValue());
        work.setProduct(handleProductStringToProductObject());
        work.setGoalAmount(Integer.parseInt(amountTextField.getText()));
        work.setDeadline(ProjectUtility.getDate(deadlineDatePicker.getValue()));
        work.setNote(noteTextArea.getText());
        System.out.println(work);
    }

    private Product handleProductStringToProductObject(){
        String[] values = productComboBox.getValue().split(" ");
        Products.addFilter("product_name", values[0]);
        Products.addFilter("size", Integer.parseInt(values[1]));
        try {
            return Products.toList(Products.getFilteredData()).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validate(){
        if (amountTextField.getText().isEmpty()){
            promptLabel.setText("กรุณากรอกจำนวนงาน");
            return false;
        }
        if (deadlineDatePicker.getValue().isBefore(LocalDate.now())){
            promptLabel.setText("กรุณากรอกกำหนดส่งให้ถูกต้อง");
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
