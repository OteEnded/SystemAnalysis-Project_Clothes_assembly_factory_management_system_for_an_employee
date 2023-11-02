package ku.cs.controller.employer.work;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.model.Material;
import ku.cs.model.MaterialUsage;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.tableview.WorkWrapper;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class MaterialPreparationPageController {
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


    @FXML
    void initialize() throws SQLException {
        detailPane.setVisible(false);
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        display_product.setCellValueFactory(new PropertyValueFactory<>("display_product"));
        goal_amount.setCellValueFactory(new PropertyValueFactory<>("goal_amount"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
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

    }

    private void showListView(String value) throws SQLException {
        materialListView.getItems().clear();
        total_materialListView.getItems().clear();
        Product product = handleProductStringToProductObject(value);
        WorkWrapper selectedWork = tableView.getSelectionModel().getSelectedItem();
        Work work = Works.getData().get(selectedWork.getId());
        for (MaterialUsage materialUsage : product.getMaterialsUsed()){
            Material material = materialUsage.getMaterial();
            material.getName();
            materialListView.getItems().add(material.getName() + " " + materialUsage.getAmountPerYield() + " " + materialUsage.getMaterial().getUnitName());
            total_materialListView.getItems().add(material.getName() + " " + materialUsage.getExpectedMaterialUsedByWork(work) + " " + materialUsage.getMaterial().getUnitName());
        }
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

    private ObservableList<WorkWrapper> fetchData() throws SQLException {

        Works.addFilter("status", Works.status_waitForMaterial);
        HashMap<String, Work> works = Works.getFilteredData();

        ObservableList<WorkWrapper> workWrappers = FXCollections.observableArrayList();
        for(String workId : works.keySet()) {
            Work work = works.get(workId);
            WorkWrapper workWrapper = new WorkWrapper(work);
            workWrappers.add(workWrapper);
        }
        return workWrappers;

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
