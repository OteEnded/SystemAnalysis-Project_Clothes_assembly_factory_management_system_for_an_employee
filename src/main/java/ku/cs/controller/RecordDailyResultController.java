package ku.cs.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;

public class RecordDailyResultController {

    @FXML private TableView<RecordDailyWrapper> tableView;
    @FXML private TableColumn<RecordDailyWrapper, String> type;
    @FXML private TableColumn<RecordDailyWrapper, String> product;
    @FXML private TableColumn<RecordDailyWrapper, Integer> quantity;
    @FXML private TableColumn<RecordDailyWrapper, Integer> capacity;
    @FXML private TableColumn<RecordDailyWrapper, LocalDate> deadline;
    @FXML private TableColumn<RecordDailyWrapper, TextField> dailyRecord;

    private int records;

    @FXML void initialize() {
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        product.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        dailyRecord.setCellValueFactory(new PropertyValueFactory<>("dailyRecord"));
        ObservableList<RecordDailyWrapper> works = FXCollections.observableArrayList();
        for(int i = 0; i < 3;i++){
            TextField textField = new TextField();
            textField.setMaxWidth(50);
            textField.setAlignment(Pos.CENTER);
            works.add(new RecordDailyWrapper(
                    "งานธรรมดา",
                    "กระโปรง ขนาด 20 นิ้ว",
                    20,
                    12,
                    LocalDate.now(),
                    textField
            ));
        }

        records = works.size();
        tableView.setItems(works);
    }

    @FXML private void handleRecordDailyResultBtn() throws IOException {
        for(int i = 0; i < records; i++){
            TextField textField = (TextField) tableView.getColumns().get(5).getCellData(i);
            System.out.println(textField.getText());
        }
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




}




