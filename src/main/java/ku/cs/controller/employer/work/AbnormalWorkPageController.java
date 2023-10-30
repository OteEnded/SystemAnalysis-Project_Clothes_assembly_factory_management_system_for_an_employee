package ku.cs.controller.employer.work;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.tableview.WorkWrapper;

import java.io.IOException;
import java.time.LocalDate;

public class AbnormalWorkPageController {

    @FXML private TableView<WorkWrapper> tableView;
    @FXML private TableColumn<WorkWrapper, String> type;
    @FXML private TableColumn<WorkWrapper, String> product;
    @FXML private TableColumn<WorkWrapper, Integer> quantity;
    @FXML private TableColumn<WorkWrapper, LocalDate> deadline;

    // work detail
    @FXML private Label workTypeLabel;
    @FXML private Label productLabel;
    @FXML private Label deadlineLabel;
    @FXML private Label amountLabel;

    @FXML
    void initialize() {
        // Bez Code
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        product.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));

//        ObservableList<WorkWrapper> works = FXCollections.observableArrayList();
//        works.add(new WorkWrapper("งานธรรมดา", "กระโปรง ขนาด 20 นิ้ว", 20, LocalDate.now(), "ทันตามกำหนด", 10, "note"));
//        works.add(new WorkWrapper("งานธรรมดา", "กระโปรง ขนาด 20 นิ้ว", 20, LocalDate.now(), "ทันตามกำหนด", 10, "note"));
//        tableView.setItems(works);
        //handleSelectedRow();
    }
//
//    private void handleSelectedRow() {
//        tableView.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> {
//                    showSelectedRow(newValue);
//                }
//        );
//    }
//
//    private void showSelectedRow(Work newValue) {
//        // productLabel.setText(newValue.getProduct());
//        productLabel.setText(newValue.toString());
//    }

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
            com.github.saacsos.FXRouter.goTo("order");
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
