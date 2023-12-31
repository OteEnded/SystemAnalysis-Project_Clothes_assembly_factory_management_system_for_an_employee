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
import ku.cs.utility.ProjectUtility;

public class FinishedWorkController {

    @FXML private TableView<WorkWrapper> tableView;
    @FXML private TableColumn<WorkWrapper, String> type;
    @FXML private TableColumn<WorkWrapper, String> display_product;
    @FXML private TableColumn<WorkWrapper, Integer> goal_amount;

    @FXML private Button sendWorkBtn;
    // work detail
    @FXML private AnchorPane detailPane;
    @FXML private Label workTypeLabel;
    @FXML private Label productLabel;
    @FXML private Label deadlineLabel;
    @FXML private Label amountLabel;

    @FXML private Text noteText;
    @FXML private ListView<String> materialListView;
    @FXML private ListView<String> total_materialListView;

    @FXML
    void initialize() throws SQLException {
        Works.load();
        detailPane.setVisible(false);
        sendWorkBtn.setVisible(false);
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        display_product.setCellValueFactory(new PropertyValueFactory<>("display_product"));
        goal_amount.setCellValueFactory(new PropertyValueFactory<>("goal_amount"));
        tableView.setItems(fetchData());
        handleSelectedRow();
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
        sendWorkBtn.setVisible(true);
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
        Work work = new Work();
        work.load(selectedWork.getId());

        // เพิ่มช้อมูลเข้าลิสต์
        for (MaterialUsage materialUsage : product.getMaterialsUsed()){
            Material material = materialUsage.getMaterial();
            material.getName();
            materialListView.getItems().add(material.getName() + " " + materialUsage.getAmountPerYield() + " " + materialUsage.getMaterial().getUnitName());
            total_materialListView.getItems().add(material.getName() + " " + materialUsage.getExpectedMaterialUsedByWork(work) + " " + materialUsage.getMaterial().getUnitName());
        }
    }

    @FXML private void handleSendWorkBtn() throws SQLException, ParseException {
        WorkWrapper selectedWork = tableView.getSelectionModel().getSelectedItem();
        ProjectUtility.debug("####", selectedWork.getId());
        Work work = new Work();
        work.load(selectedWork.getId());
//
//        Work work = Works.getData().get(selectedWork.getId());
        if (work != null){
            work.setStatus(Works.status_sent);
            Works.save(work);
        }
        tableView.setItems(fetchData());
        tableView.refresh();
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

    @FXML private void handleDailyRecordBtn() throws IOException {
        try {
            com.github.saacsos.FXRouter.goTo("record-daily-result");
        } catch (Exception e) {
            System.err.println("ไปหน้า received-work ไม่ได้");
            e.printStackTrace();
        }
    }

    private ObservableList<WorkWrapper> fetchData() throws SQLException {
        Works.load();
        Works.addFilter("status", Works.status_done);
        List<Work> works =  Works.getSortedBy("deadline", Works.getFilteredData());
        System.out.println(works);

        ObservableList<WorkWrapper> workWrappers = FXCollections.observableArrayList();
        for(Work work : works) {
            WorkWrapper workWrapper = new WorkWrapper(work);
            workWrappers.add(workWrapper);
        }
        return workWrappers;
    }

}
