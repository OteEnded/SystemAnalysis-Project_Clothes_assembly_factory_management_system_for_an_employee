package ku.cs.controller.employer;

import com.github.saacsos.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
        if (com.github.saacsos.FXRouter.getData() != null){
            initForRepairWork();
        }
        else initForAddWork();


    }

    void initForAddWork() throws SQLException {
        headerText.setText("เพิ่มงาน");
        workTypeComboBox.getItems().clear();
        workTypeComboBox.getItems().addAll(Works.type_normal, Works.type_rush);
        workTypeComboBox.getSelectionModel().select(Works.type_normal);
        for (Product product : Products.getDataAsList()){
            productComboBox.getItems().addAll(product.getName() + " "
                    + product.getSize() + " นิ้ว");
        }
        promptLabel.setText("");
    }

    void initForRepairWork() throws SQLException {
        headerText.setText("เพิ่มงานแก้");
        workTypeComboBox.getItems().clear();
        workTypeComboBox.getItems().addAll(Works.type_repair);
        workTypeComboBox.getSelectionModel().select(Works.type_repair);
        if (FXRouter.getData() != null){
            Product product = ((Work)FXRouter.getData()).getProduct();
            productComboBox.getSelectionModel().select(product.getName() + " "
                    + product.getSize() + " นิ้ว");
        }
    }

    @FXML
    public void handleSubmitButton() throws SQLException, ParseException, IOException {

//        PopUpUtility.popUp("loading", "กำลังบันทึกข้อมูล...");

        ProjectUtility.debug("###############################");
        ProjectUtility.debug(validate());

        if (validate()) {
            promptLabel.setText("");
//            if (!addWork()){
//                // pop up
//            }

            addWork();

            try {
                com.github.saacsos.FXRouter.goTo("wait-for-receive");
            } catch (Exception e){
                System.err.println("ไปหน้า wait-for-receive ไม่ได้");
                e.printStackTrace();
            }
        }
    }

    private boolean addWork() throws SQLException, ParseException {
        Work work = new Work();
        work.setStatus(Works.status_waitForAccept);
        work.setWorkType(workTypeComboBox.getValue());
        work.setProduct(handleProductStringToProductObject());
        work.setGoalAmount(Integer.parseInt(amountTextField.getText()));
        work.setDeadline(ProjectUtility.getDate(deadlineDatePicker.getValue()));
        work.setNote(noteTextArea.getText());
        if (com.github.saacsos.FXRouter.getData() != null){
            work.setRepairWork((Work) FXRouter.getData());
        }

        if (work.getProduct().getProgressRate() != -1){
            if (work.getEstimated().equals(Works.estimate_late)){
                return false;
            }
        }
        work.save();
        System.out.println(work);
        return true;
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
        if (productComboBox.getSelectionModel().isEmpty()){
            promptLabel.setText("กรุณาเลือกสินค้าที่ต้องการสั่งผลิต");
            return false;
        }
        if (amountTextField.getText().isEmpty()){
            promptLabel.setText("กรุณากรอกจำนวนงาน");
            return false;
        }
        if (deadlineDatePicker.getValue().isBefore(LocalDate.now())){
            promptLabel.setText("กรุณากรอกวันกำหนดส่งให้ถูกต้อง");
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
