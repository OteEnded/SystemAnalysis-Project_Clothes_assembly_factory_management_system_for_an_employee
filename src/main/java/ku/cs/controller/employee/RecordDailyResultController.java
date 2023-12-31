package ku.cs.controller.employee;

import com.mysql.cj.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.entity.DailyRecords;
import ku.cs.entity.Works;
import ku.cs.model.DailyRecord;
import ku.cs.model.Work;
import ku.cs.tableview.RecordDailyWrapper;
import ku.cs.tableview.WorkWrapper;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class RecordDailyResultController {

    @FXML private TableView<RecordDailyWrapper> tableView;
    @FXML private TableColumn<WorkWrapper, String> type;
    @FXML private TableColumn<WorkWrapper, String> display_product;
    @FXML private TableColumn<WorkWrapper, Integer> goal_amount;
    @FXML private TableColumn<WorkWrapper, LocalDate> deadline;
    @FXML private TableColumn<WorkWrapper, Integer> progress_amount;
    @FXML private TableColumn<RecordDailyWrapper, TextField> dailyRecord;
    @FXML private Label promptLabel;

    private final ArrayList<Work> allWorks = new ArrayList<>();

    @FXML void initialize() throws SQLException {
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        display_product.setCellValueFactory(new PropertyValueFactory<>("display_product"));
        goal_amount.setCellValueFactory(new PropertyValueFactory<>("goal_amount"));
        progress_amount.setCellValueFactory(new PropertyValueFactory<>("progress_amount"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        dailyRecord.setCellValueFactory(new PropertyValueFactory<>("dailyRecord"));
        ObservableList<RecordDailyWrapper> recordDailyWrappers = fetchData();
        tableView.setItems(recordDailyWrappers);
    }

    @FXML private void handleRecordDailyResultBtn() throws IOException, SQLException, ParseException {
        for(int i = 0; i < allWorks.size(); i++){
            TextField textField = (TextField) tableView.getColumns().get(5).getCellData(i);
            boolean checkNumeric = Pattern.matches("[0-9]+", textField.getText());
            //check numeric
            if(checkNumeric) {
                //check out of bound
                if(Long.parseLong(textField.getText()) > Integer.MAX_VALUE) {
                    promptLabel.setText("กรุณากรอกจำนวนให้ถูกต้อง (out of bound)");
                    return;
                }
                int daily = Integer.parseInt(textField.getText());
                Work work = allWorks.get(i);

                if(work.getProgressAmount() + daily > work.getGoalAmount() || daily < 0) {
                    promptLabel.setText("กรุณากรอกจำนวนให้ถูกต้อง");
                    return;
                }

                DailyRecord dailyRecord = new DailyRecord();
                dailyRecord.setDate(ProjectUtility.getDate());
                dailyRecord.setForWork(work);
                dailyRecord.setAmount(daily);
                dailyRecord.save();

                try {
                    com.github.saacsos.FXRouter.goTo("work-in-progress-work");
                } catch (Exception e) {
                    System.err.println("ไปหน้า work-in-progress-work ไม่ได้");
                    e.printStackTrace();
                }
            } else {
                promptLabel.setText("กรุณากรอกจำนวนให้ถูกต้อง");
            }
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

    private ObservableList<RecordDailyWrapper> fetchData() throws SQLException {

        Works.addFilter("status", Works.status_working);
        List<Work> works =  Works.getSortedBy("deadline", Works.getFilteredData());

        ObservableList<RecordDailyWrapper> recordDailyWrappers = FXCollections.observableArrayList();
        for(Work work : works) {
            allWorks.add(work);
            TextField dailyRecord = new TextField();

            dailyRecord.textProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    int daily = Integer.parseInt(newValue);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if(work.getProgressAmount() + Integer.parseInt(newValue) > work.getGoalAmount()) {
                    dailyRecord.setText(String.valueOf(work.getRemainingAmount()));
                }
            });
            dailyRecord.setPrefWidth(200);
            RecordDailyWrapper recordDailyWrapper = new RecordDailyWrapper(work, dailyRecord);
            recordDailyWrappers.add(recordDailyWrapper);
        }
        return recordDailyWrappers;
    }




}




