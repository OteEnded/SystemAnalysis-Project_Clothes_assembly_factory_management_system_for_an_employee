package ku.cs.controller.employer.work;

import javafx.fxml.FXML;

import java.io.IOException;

public class MaterialPreparationPageController {

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
