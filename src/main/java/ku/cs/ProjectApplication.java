package ku.cs;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

import com.github.saacsos.FXRouter;
import ku.cs.utility.ProjectUtility;

public class ProjectApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        if (!isScreenBigEnoughToShowStage()) return;

        stage.setResizable(false);
        stage.getIcons().add(ProjectUtility.getProgramIcon());

        if (!ProjectUtility.connectDB()) return;

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
        FXRouter.when("order", packageStr + "order-page.fxml");
        FXRouter.when("received-work", packageStr + "employee/received-work-page.fxml");
        FXRouter.when("waiting-for-material-work", packageStr + "employee/waiting-for-material-work-page.fxml");
        FXRouter.when("work-in-progress-work", packageStr + "employee/work-in-progress-work-page.fxml");
        FXRouter.when("finished-work", packageStr + "employee/finished-work-page.fxml");
        FXRouter.when("waiting-for-check-work", packageStr + "employee/waiting-for-check-work-page.fxml");
        FXRouter.when("checked-work", packageStr + "employee/checked-work-page.fxml");
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
