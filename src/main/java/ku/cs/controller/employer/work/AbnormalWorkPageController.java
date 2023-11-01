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
import ku.cs.entity.Works;
import ku.cs.model.Work;
import ku.cs.tableview.WorkWrapper;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class AbnormalWorkPageController {

    @FXML private TableView<WorkWrapper> tableView;
    @FXML private TableColumn<WorkWrapper, String> type;
    @FXML private TableColumn<WorkWrapper, String> display_product;
    @FXML private TableColumn<WorkWrapper, Integer> goal_amount;
    @FXML private TableColumn<WorkWrapper, LocalDate> deadline;
    @FXML private TableColumn<WorkWrapper, String> estimate;

    // work detail
    @FXML private AnchorPane detailPane;
    @FXML private Label workTypeLabel;
    @FXML private Label productLabel;
    @FXML private Label deadlineLabel;
    @FXML private Label amountLabel;
    @FXML private ListView yieldListView;
    @FXML private ListView materialListView;




    @FXML
    void initialize() throws SQLException {
        detailPane.setVisible(false);
        // Bez code
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        display_product.setCellValueFactory(new PropertyValueFactory<>("display_product"));
        goal_amount.setCellValueFactory(new PropertyValueFactory<>("goal_amount"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        estimate.setCellValueFactory(new PropertyValueFactory<>("estimate"));
        tableView.setItems(fetchData());
        handleSelectedRow();
    }

    private void handleSelectedRow() {
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null) {
                        showSelectedRow(newValue);
                    }
                }
        );
    }

    private void showSelectedRow(WorkWrapper newValue) {
        detailPane.setVisible(true);
        workTypeLabel.setText(newValue.getType());
        productLabel.setText(newValue.getDisplay_product());
        deadlineLabel.setText(newValue.getDeadline().toString());
        amountLabel.setText(String.valueOf(newValue.getGoal_amount()));
    }

    private ObservableList<WorkWrapper> fetchData() throws SQLException {

        HashMap<String, Work> works = Works.getAbnormalWorks();

        ObservableList<WorkWrapper> workWrappers = FXCollections.observableArrayList();
        for(String workId : works.keySet()) {
            Work work = works.get(workId);
            WorkWrapper workWrapper = new WorkWrapper(work);
            workWrappers.add(workWrapper);
        }
        return workWrappers;

    }

    @FXML private void handleEditWorkButton(){
        try {
            WorkWrapper selectedWork = tableView.getSelectionModel().getSelectedItem();
            Work work = Works.getData().get(selectedWork.getId());
            com.github.saacsos.FXRouter.goTo("edit-work", work);
        } catch (Exception e){
            System.err.println("ไปหน้า edit-work ไม่ได้");
            e.printStackTrace();
        }
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
