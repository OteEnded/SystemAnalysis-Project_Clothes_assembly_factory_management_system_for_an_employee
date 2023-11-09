package ku.cs.controller.employer;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import ku.cs.entity.Materials;
import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.model.Material;
import ku.cs.model.MaterialUsage;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

public class EditWorkPageController {

    @FXML private Text headerText;
    @FXML private ComboBox<String> workTypeComboBox;
    @FXML private Label productLabel;
    @FXML private TextField amountTextField;
    @FXML private DatePicker deadlineDatePicker;
    @FXML private TextArea noteTextArea;

    @FXML private Label promptLabel;

    @FXML
    void initialize() throws SQLException {
        if (com.github.saacsos.FXRouter.getData() != null) {
            Work work = (Work) com.github.saacsos.FXRouter.getData();
            workTypeComboBox.getItems().clear();
            workTypeComboBox.getItems().addAll(Works.type_normal, Works.type_rush);
            workTypeComboBox.getSelectionModel().select(work.getWorkType());

            Product product = work.getProduct();
            productLabel.setText(product.getName()+ " " + product.getSize() + " นิ้ว");

            amountTextField.setText(String.valueOf(work.getGoalAmount()));
            noteTextArea.setText(work.getNote());

            amountTextField.textProperty().addListener((ov, oldValue, newValue) -> {
                deadlineDatePicker.setDisable(Objects.equals(newValue, "")
                        || Integer.parseInt(newValue) < 1);
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


    }

    @FXML
    public void handleSubmitButton() throws SQLException, ParseException {
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
        Work work = (Work) com.github.saacsos.FXRouter.getData();
        work.setWorkType(workTypeComboBox.getValue());
        work.setGoalAmount(Integer.parseInt(amountTextField.getText()));
        work.setDeadline(ProjectUtility.getDate(deadlineDatePicker.getValue()));
        work.setNote(noteTextArea.getText());

//        if (work.getProduct().getProgressRate() != -1){
//            if (work.getEstimated().equals(Works.estimate_late)){
//                return false;
//            }
//        }
        work.save();
        System.out.println(work);
        return true;
    }


    private boolean validate(){
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

    private void setRecommendedDateToDatePicker() throws SQLException {
        String[] values = productLabel.getText().split(" ");
        Products.addFilter("product_name", values[0]);
        Products.addFilter("size", Integer.parseInt(values[1]));
        Work work = new Work();
        work.setProduct(Products.toList(Products.getFilteredData()).get(0));
        int amount = Integer.parseInt(amountTextField.getText());
        double progress_rate = work.getProduct().getProgressRate();
        System.out.println(progress_rate * amount);
        if(progress_rate == -1) {
            deadlineDatePicker.setValue(ProjectUtility.getDateWithOffset(LocalDate.now(), 1).toLocalDate());
        } else {
            deadlineDatePicker.setValue(ProjectUtility.getDateWithOffset(LocalDate.now(), (int) (amount * progress_rate)).toLocalDate());
        }
    }

    // MenuBar Handle

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

    // Sub-MenuBar Handle

    @FXML
    public void handleWaitForReceiveButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("wait-for-receive");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAbnormalWorkButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("abnormal-work");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMaterialPreparationButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("material-preparation");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleWorkInProgressButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("work-in-progress");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleReviewWorkButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("review-work");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCompleteWorkButton() throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("complete-work");
        } catch (Exception e){
            System.err.println("ไปหน้า home ไม่ได้");
            e.printStackTrace();
        }
    }


}
