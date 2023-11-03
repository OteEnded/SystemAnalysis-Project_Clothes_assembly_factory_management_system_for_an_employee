package ku.cs.controller.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.github.saacsos.FXRouter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.model.Material;
import ku.cs.model.MaterialUsage;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.tableview.WorkWrapper;


public class WaitingForMaterialWorkController {

    @FXML private TableView<WorkWrapper> tableView;
    @FXML private TableColumn<WorkWrapper, String> type;
    @FXML private TableColumn<WorkWrapper, String> display_product;
    @FXML private TableColumn<WorkWrapper, Integer> goal_amount;
    @FXML private TableColumn<WorkWrapper, LocalDate> deadline;

    // work detail
    @FXML private AnchorPane detailPane;
    @FXML private Label workTypeLabel;
    @FXML private Label productLabel;
    @FXML private Label deadlineLabel;
    @FXML private Label amountLabel;

    @FXML private Text noteText;
    @FXML private ListView<String> materialListView;
    @FXML private ListView<String> total_materialListView;

    @FXML private Button submitReceivedMaterialBtn;

    @FXML
    void initialize() throws SQLException {

        submitReceivedMaterialBtn.setVisible(false);

        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        display_product.setCellValueFactory(new PropertyValueFactory<>("display_product"));
        goal_amount.setCellValueFactory(new PropertyValueFactory<>("goal_amount"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        tableView.setItems(fetchData());
        handleSelectedRow();
    }

    @FXML private void handleSubmitReceivedMaterialBtn() throws SQLException, ParseException {
        WorkWrapper selectedWork = tableView.getSelectionModel().getSelectedItem();
        Work work = Works.getData().get(selectedWork.getId());
        System.out.println(work);

        work.setStatus(Works.status_working);
        Works.save(work);

        tableView.setItems(fetchData());
        tableView.refresh();
    }


    private void handleSelectedRow() {
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null) {
                        try {
                            showSelectedRow(newValue);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    private void showSelectedRow(WorkWrapper newValue) throws SQLException {
        detailPane.setVisible(true);
        workTypeLabel.setText(newValue.getType());
        productLabel.setText(newValue.getDisplay_product());
        deadlineLabel.setText(newValue.getDeadline().toString());
        amountLabel.setText(String.valueOf(newValue.getGoal_amount()));
        showListView(newValue.getDisplay_product());
        noteText.setText(newValue.getNote());
        submitReceivedMaterialBtn.setVisible(true);
    }

    private Product handleProductStringToProductObject(String value){
        String[] values = value.split(" ");
        Products.addFilter("product_name", values[0]);
        Products.addFilter("size", Integer.parseInt(values[2]));
        try {
            return Products.toList(Products.getFilteredData()).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showListView(String value) throws SQLException {
        materialListView.getItems().clear();
        total_materialListView.getItems().clear();
        // เรียกข้อมูล
        Product product = handleProductStringToProductObject(value);
        WorkWrapper selectedWork = tableView.getSelectionModel().getSelectedItem();
        Work work = Works.getData().get(selectedWork.getId());

        // เพิ่มช้อมูลเข้าลิสต์
        for (MaterialUsage materialUsage : product.getMaterialsUsed()){
            Material material = materialUsage.getMaterial();
            material.getName();
            materialListView.getItems().add(material.getName() + " " + materialUsage.getAmountPerYield() + " " + materialUsage.getMaterial().getUnitName());
            total_materialListView.getItems().add(material.getName() + " " + materialUsage.getExpectedMaterialUsedByWork(work) + " " + materialUsage.getMaterial().getUnitName());
        }
    }

    /* Navbar Btn */
    @FXML private void handleReceivedWorkBtn() throws IOException {
        try {
            FXRouter.goTo("received-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า received-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleWaitingForMaterialWorkBtn() throws IOException {
        try {
            FXRouter.goTo("waiting-for-material-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า waiting-for-material-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleWorkInProgressWorkBtn() throws IOException {
        try {
            FXRouter.goTo("work-in-progress-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า work-in-progress-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleFinishedWorkBtn() throws IOException {
        try {
            FXRouter.goTo("finished-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า finished-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleWaitingForCheckWorkBtn() throws IOException {
        try {
            FXRouter.goTo("waiting-for-check-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า waiting-for-check-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleCheckedWorkBtn() throws IOException {
        try {
            FXRouter.goTo("checked-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า checked-work ไม่ได้");
            e.printStackTrace();
        }
    }

    private ObservableList<WorkWrapper> fetchData() throws SQLException {

        Works.addFilter("status", Works.status_waitForMaterial);
        List<Work> works =  Works.getSortedBy("deadline", Works.getFilteredData());

        ObservableList<WorkWrapper> workWrappers = FXCollections.observableArrayList();
        for(Work work : works) {
            WorkWrapper workWrapper = new WorkWrapper(work);
            workWrappers.add(workWrapper);
        }
        return workWrappers;
    }

}
