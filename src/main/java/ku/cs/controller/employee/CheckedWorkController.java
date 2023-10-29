package ku.cs.controller.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import ku.cs.controller.Work;

import java.io.IOException;
import java.time.LocalDate;

public class CheckedWorkController {
    @FXML
    private TableView<Work> tableView;
    @FXML private TableColumn<Work, String> type;
    @FXML private TableColumn<Work, String> product;
    @FXML private TableColumn<Work, Integer> quantity;
    @FXML private TableColumn<Work, LocalDate> deadline;
    @FXML private TableColumn<Work, String> status;
    @FXML private Label workDetail;

    @FXML
    void initialize() {

        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        product.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
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
    }

    /* Navbar Btn */
    @FXML private void handleReceivedWorkBtn() throws IOException {
        try {
            com.github.saacsos.FXRouter.goTo("received-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า received-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleWaitingForMaterialWorkBtn() throws IOException {
        try {
            com.github.saacsos.FXRouter.goTo("waiting-for-material-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า waiting-for-material-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleWorkInProgressWorkBtn() throws IOException {
        try {
            com.github.saacsos.FXRouter.goTo("work-in-progress-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า work-in-progress-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleFinishedWorkBtn() throws IOException {
        try {
            com.github.saacsos.FXRouter.goTo("finished-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า finished-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleWaitingForCheckWorkBtn() throws IOException {
        try {
            com.github.saacsos.FXRouter.goTo("waiting-for-check-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า waiting-for-check-work ไม่ได้");
            e.printStackTrace();
        }
    }

    @FXML private void handleCheckedWorkBtn() throws IOException {
        try {
            com.github.saacsos.FXRouter.goTo("checked-work");
        } catch (Exception e) {
            System.err.println("ไปหน้า checked-work ไม่ได้");
            e.printStackTrace();
        }
    }
}
