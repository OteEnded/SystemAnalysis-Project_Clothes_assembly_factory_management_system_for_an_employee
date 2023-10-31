package ku.cs.controller.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;

import com.github.saacsos.FXRouter;
import ku.cs.entity.Works;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.tableview.WorkWrapper;

public class ReceivedWorkController {

    @FXML private TableView<WorkWrapper> tableView;
    @FXML private TableColumn<WorkWrapper, String> type;
    @FXML private TableColumn<WorkWrapper, String> display_product;
    @FXML private TableColumn<WorkWrapper, Integer> goal_amount;
    @FXML private TableColumn<WorkWrapper, LocalDate> deadline;
    @FXML private TableColumn<WorkWrapper, String> estimate;

    @FXML private Button acceptBtn;
    @FXML private Button putWorkRateBtn;
    @FXML private Label workDetail;

    @FXML
    void initialize() throws SQLException {

        acceptBtn.setVisible(false);
        putWorkRateBtn.setVisible(false);

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
    @FXML
    private void handleAcceptWorkBtn() throws SQLException, ParseException {
        WorkWrapper selectedWork = tableView.getSelectionModel().getSelectedItem();
        Work work = Works.getData().get(selectedWork.getId());
        work.setStatus(Works.status_waitForMaterial);
        Works.save(work);
        tableView.setItems(fetchData());
        tableView.refresh();
    }

    private void showSelectedRow(WorkWrapper newValue) {
        workDetail.setText(newValue.toString());
        if(newValue.getEstimate().equals("ทันเวลา")) {
            acceptBtn.setVisible(true);
            putWorkRateBtn.setVisible(false);
        } else if (newValue.getEstimate().equals("ไม่พบอัตราการทำงาน")) {
            acceptBtn.setVisible(false);
            putWorkRateBtn.setVisible(true);
        }
    }
    @FXML
    private void handlePutWorkRateBtn(ActionEvent actionEvent) throws IOException {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/fxml/employee/put-work-rate-dialog.fxml"));
        dialog.setDialogPane(loader.load());
        dialog.setResizable(false);
        if (dialog.isShowing()) {
            // Close the dialog.
            dialog.close();
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

        Works.addFilter("status", Works.status_waitForAccept);
        HashMap<String, Work> works = Works.getFilteredData();

        ObservableList<WorkWrapper> workWrappers = FXCollections.observableArrayList();
        for(String workId : works.keySet()) {
            Work work = works.get(workId);
            WorkWrapper workWrapper = new WorkWrapper(work);
            workWrappers.add(workWrapper);
        }
        return workWrappers;
    }

}
