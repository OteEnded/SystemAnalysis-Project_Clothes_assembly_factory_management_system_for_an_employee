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
        works.add(new Work("งานธรรมดา", "กระโปรง ขนาด 20 นิ้ว", 20, LocalDate.now(), "ทันตามกำหนด"));
        works.add(new Work("งานธรรมดา", "กางเกงขายาวสีดำ ขนาด 32 นิ้ว", 10, LocalDate.now(), "ไม่พบอัตราการทำงาน"));
        works.add(new Work("งานธรรมดา", "กางเกงขายาวสีดำ ขนาด 32 นิ้ว", 10, LocalDate.now(), "ไม่พบอัตราการทำงาน"));
        works.add(new Work("งานธรรมดา", "กางเกงขายาวสีดำ ขนาด 32 นิ้ว", 10, LocalDate.now(), "ไม่พบอัตราการทำงาน"));
        works.add(new Work("งานธรรมดา", "กางเกงขายาวสีดำ ขนาด 32 นิ้ว", 10, LocalDate.now(), "ไม่พบอัตราการทำงาน"));
        works.add(new Work("งานธรรมดา", "กางเกงขายาวสีดำ ขนาด 32 นิ้ว", 10, LocalDate.now(), "ไม่พบอัตราการทำงาน"));
        works.add(new Work("งานธรรมดา", "กางเกงขายาวสีดำ ขนาด 32 นิ้ว", 10, LocalDate.now(), "ไม่พบอัตราการทำงาน"));
        works.add(new Work("งานธรรมดา", "กางเกงขายาวสีดำ ขนาด 32 นิ้ว", 10, LocalDate.now(), "ไม่พบอัตราการทำงาน"));        works.add(new Work("งานธรรมดา", "กางเกงขายาวสีดำ ขนาด 32 นิ้ว", 10, LocalDate.now(), "ไม่พบอัตราการทำงาน"));
        works.add(new Work("งานธรรมดา", "กางเกงขายาวสีดำ ขนาด 32 นิ้ว", 10, LocalDate.now(), "ไม่พบอัตราการทำงาน"));
        tableView.setItems(works);
        handleSelectedRow();
    }

    private void handleSelectedRow() {
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println(newValue);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/fxml/put-work-rate-dialog.fxml"));
        dialog.setDialogPane(loader.load());
        dialog.setResizable(false);
        if (dialog.isShowing()) {
            // Close the dialog.
            dialog.close();
        }
    }
}
