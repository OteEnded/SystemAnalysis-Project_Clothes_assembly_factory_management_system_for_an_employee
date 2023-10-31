package ku.cs;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import com.github.saacsos.FXRouter;
import ku.cs.utility.ProjectUtility;

public class ProjectApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException, SQLException {

        if (!isScreenBigEnoughToShowStage()) return;

        ProjectUtility.setStage(stage);
        stage.setResizable(false);
        stage.getIcons().add(ProjectUtility.getProgramIcon());

        if (!ProjectUtility.connectDB()) return;
        ProjectUtility.loadDBBuffer();

        FXRouter.bind(this, stage, "ระบบจัดการทำงานในโรงงานประกอบผ้า สำหรับลูกจ้างหนึ่งคน", ProjectUtility.programWidth, ProjectUtility.programHeight);
        configRoute();

//        User user = new User();
//        user.load("Ote");
//        FXRouter.goTo("change-forgotten-password", user);

        FXRouter.goTo("received-work");
    }

    private static void configRoute() {
        String packageStr = "ku/cs/fxml/";
        FXRouter.when("home", packageStr + "home-page.fxml");
        FXRouter.when("order", packageStr + "employer/order-page.fxml");
        FXRouter.when("product-manage", packageStr + "employer/product-view-page.fxml");
        FXRouter.when("add-product",packageStr+"employer/add-product-page.fxml");
        FXRouter.when("material-manage", packageStr + "employer/material-view-page.fxml");
        FXRouter.when("wait-for-receive", packageStr + "employer/work-management/wait-for-receive-page.fxml");
        FXRouter.when("abnormal-work", packageStr + "employer/work-management/abnormal-work-view-page.fxml");
        FXRouter.when("material-preparation", packageStr + "employer/work-management/material-preparation-view-page.fxml");
        FXRouter.when("work-in-progress", packageStr + "employer/work-management/work-in-progress-view-page.fxml");
        FXRouter.when("review-work", packageStr + "employer/work-management/review-work-page.fxml");
        FXRouter.when("complete-work", packageStr + "employer/work-management/complete-work-view-page.fxml");

        FXRouter.when("received-work", packageStr + "employee/received-work-page.fxml");
        FXRouter.when("waiting-for-material-work", packageStr + "employee/waiting-for-material-work-page.fxml");
        FXRouter.when("work-in-progress-work", packageStr + "employee/work-in-progress-work-page.fxml");
        FXRouter.when("finished-work", packageStr + "employee/finished-work-page.fxml");
        FXRouter.when("waiting-for-check-work", packageStr + "employee/waiting-for-check-work-page.fxml");
        FXRouter.when("checked-work", packageStr + "employee/checked-work-page.fxml");
        FXRouter.when("record-daily-result", packageStr + "employee/record-daily-result-page.fxml");
    }

    private static boolean isScreenBigEnoughToShowStage(){ //โค้ดส่วนป้องกันไม่ให้โปรแกรมถูกเปิดบนหน้าจอที่มีขนาดเล็กกว่าหน้าต่างของโปรแกรม - Ote
        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        if ((screenSize.getWidth() < ProjectUtility.programWidth) || (screenSize.getHeight() < ProjectUtility.programHeight)) {
            Alert screenSizeAlert = new Alert(Alert.AlertType.ERROR);
            screenSizeAlert.setTitle("พบปัญหา: ไม่สามารถเปิดโปรแกรมได้");
            screenSizeAlert.setHeaderText("ไม่สามารถแสดงผลหน้าต่างของโปรแกรมนี้ได้");
            screenSizeAlert.setContentText("ขออภัย คุณไม่สามารถเปิดโปรแกรมนี้ได้บนหน้าจอที่มีขนาดเล็กกว่า\n" +
                    ProjectUtility.programWidth + " x " + ProjectUtility.programHeight + " พิกเซล ได้");
            screenSizeAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        launch();
    }
}
