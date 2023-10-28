package ku.cs.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import com.github.saacsos.FXRouter;

public class ReceivedWorkController {

    @FXML private TableView<Work> tableView;
    @FXML private TableColumn<Work, String> type;
    @FXML private TableColumn<Work, String> product;
    @FXML private TableColumn<Work, Integer> quantity;
    @FXML private TableColumn<Work, LocalDate> deadline;
    @FXML private TableColumn<Work, String> status;

    @FXML private Button acceptBtn;
    @FXML private Button putWorkRateBtn;
    @FXML private Label workDetail;

    @FXML
    void initialize() {

        acceptBtn.setVisible(false);
        putWorkRateBtn.setVisible(false);

        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        product.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<Work> works = FXCollections.observableArrayList();
        works.add(new Work("งานธรรมดา", "กระโปรง ขนาด 20 นิ้ว", 20, LocalDate.now(), "ทันตามกำหนด", 10, "note"));
        works.add(new Work("งานธรรมดา", "กระโปรง ขนาด 20 นิ้ว", 20, LocalDate.now(), "ทันตามกำหนด", 10, "note"));
        tableView.setItems(works);
        handleSelectedRow();
    }

    private void handleSelectedRow() {
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    showSelectedRow(newValue);
                }
        );
    }

    private void showSelectedRow(Work newValue) {
        workDetail.setText(newValue.toString());
        if(newValue.getStatus().equals("ทันตามกำหนด")) {
            acceptBtn.setVisible(true);
            putWorkRateBtn.setVisible(false);
        } else if (newValue.getStatus().equals("ไม่พบอัตราการทำงาน")) {
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
}
