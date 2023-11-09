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
import java.util.HashMap;
import java.util.Objects;

public class OrderWorkPageController {
    @FXML private Text headerText;
    @FXML private ComboBox<String> workTypeComboBox;
    @FXML private ComboBox<String> productComboBox;
    @FXML private TextField amountTextField;
    @FXML private DatePicker deadlineDatePicker;
    @FXML private TextArea noteTextArea;

    @FXML private Label promptLabel;

    private Work addingRepairWork;

    @FXML
    void initialize() throws SQLException {
        if (com.github.saacsos.FXRouter.getData() != null){
            initForRepairWork();
        }
        else initForAddWork();

        deadlineDatePicker.setDisable(true);

        productComboBox.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            deadlineDatePicker.setDisable(newValue == null);
            deadlineDatePicker.setDisable(Objects.equals(amountTextField.getText(), ""));
        });

       amountTextField.textProperty().addListener((ov, oldValue, newValue) -> {
           deadlineDatePicker.setDisable(Objects.equals(newValue, "")
                   || Integer.parseInt(newValue) < 1
                   || productComboBox.getValue() == null);
           try {
               setRecommendedDateToDatePicker();
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       });

       deadlineDatePicker.setDayCellFactory(picker -> new DateCell() {
           @Override
           public void updateItem(LocalDate date, boolean empty) {
               super.updateItem(date, empty);
               LocalDate tomorrow = ProjectUtility.getDate(1).toLocalDate();
               setDisable(empty || date.isBefore(tomorrow));
           }
       });

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
            addingRepairWork = (Work) FXRouter.getData();
            Product product = addingRepairWork.getProduct();
            productComboBox.getSelectionModel().select(product.getName() + " "
                    + product.getSize() + " นิ้ว");
        }
    }

    @FXML
    public void handleSubmitButton() throws SQLException, ParseException, IOException {

//        PopUpUtility.popUp("loading", "กำลังบันทึกข้อมูล...");

//        ProjectUtility.debug("###############################");
//        ProjectUtility.debug(validate());

        if (validate()) {
            promptLabel.setText("");
            if (!addWork()){
                return;
            }

            try {
                com.github.saacsos.FXRouter.goTo("wait-for-receive");
            } catch (Exception e){
                System.err.println("ไปหน้า wait-for-receive ไม่ได้");
                e.printStackTrace();
            }
        }
    }

    private boolean addWork() throws SQLException, ParseException, IOException {
        Work work = new Work();
        work.setStatus(Works.status_waitForAccept);
        work.setWorkType(workTypeComboBox.getValue());
        work.setProduct(handleProductStringToProductObject());
        work.setGoalAmount(Integer.parseInt(amountTextField.getText()));
        work.setDeadline(ProjectUtility.getDate(deadlineDatePicker.getValue()));
        work.setNote(noteTextArea.getText());
        if (addingRepairWork != null){
            addingRepairWork.setStatus(Works.status_checked);
        }

        if (work.getProduct().getProgressRate() != -1){
            if (work.getEstimated().equals(Works.estimate_late)){

                HashMap<String, Object> passingData = new HashMap<>();
                passingData.put("work", work);
                PopUpUtility.popUp("order-estimated-late", passingData);

                return false;
            }
        }
//        else {
//            Work bufferWork = new Work(work.getData());
//            bufferWork.save();
//            if (bufferWork.getRecommendedProgressRate() == 100){
//                HashMap<String, Object> passingData = new HashMap<>();
//                passingData.put("work", bufferWork);
//                PopUpUtility.popUp("order-estimated-late", passingData);
//                return false;
//            }
//            bufferWork.delete();
//        }

        work.save();
        System.out.println(work);
        return true;
    }

    private Product handleProductStringToProductObject(){
        String[] values = productComboBox.getValue().split(" ");
        Products.addFilter("product_name", values[0]);
        Products.addFilter("size", Integer.parseInt(values[1]));
        try {
            System.out.println(Products.toList(Products.getFilteredData()).get(0));
            return Products.toList(Products.getFilteredData()).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setRecommendedDateToDatePicker() throws SQLException {
        String[] values = productComboBox.getValue().split(" ");
        Products.addFilter("product_name", values[0]);
        Products.addFilter("size", Integer.parseInt(values[1]));
        Work work = new Work();
        work.setProduct(Products.toList(Products.getFilteredData()).get(0));
        int amount = Integer.parseInt(amountTextField.getText());
        double progress_rate = work.getProduct().getProgressRate();
        int day = (int) Math.ceil(amount / progress_rate) + 1;
        System.out.println(amount + "/" + progress_rate + "->" + day);
        if(progress_rate == -1) {
            deadlineDatePicker.setValue(ProjectUtility.getDateWithOffset(LocalDate.now(), 1).toLocalDate());
        } else {
            deadlineDatePicker.setValue(ProjectUtility.getDateWithOffset(LocalDate.now(), day).toLocalDate());
        }
    }

    private boolean validate() {
        if (productComboBox.getSelectionModel().isEmpty()){
            if (addingRepairWork != null) return true;
            promptLabel.setText("กรุณาเลือกสินค้าที่ต้องการสั่งผลิต");
            return false;
        }
        if (Long.parseLong(amountTextField.getText()) > Integer.MAX_VALUE) {
            promptLabel.setText("กรุณากรอกจำนวนงานให้ถูกต้อง");
            return false;
        }
        if(Integer.parseInt(amountTextField.getText()) < 1)  {
            promptLabel.setText("กรุณากรอกจำนวนงานให้ถูกต้อง");
            return false;
        }
        if(deadlineDatePicker.getValue().isBefore(LocalDate.now())){
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

    @FXML
    public void handleEmployeeButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("received-work");
        } catch (Exception e){
            System.err.println("ไปหน้า received-work ไม่ได้");
            e.printStackTrace();
        }
    }

}
