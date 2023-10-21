package ku.cs.controller;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import ku.cs.utility.ProjectUtility;

public class HomePageController {
    @FXML private AnchorPane scenePane;

    @FXML
    public void initialize(){
        ProjectUtility.debug("HomePageController[initialize]: Initializing...");
    }

    //ปุ่มไปหน้าเกี่ยวกับโปรแกรม
    @FXML
    public void handleAboutProgramButton(ActionEvent actionEvent){
        try {
            FXRouter.goTo("about");
        } catch (Exception err){
            System.out.println(err);
        }
    }
    @FXML
    public void handleDeveloperButton(ActionEvent actionEvent){
        try {
            FXRouter.goTo("developer");
        } catch (Exception err){
            System.err.println("ไปหน้า developer ไม่ได้");
            err.printStackTrace();
        }
    }
    //ปุ่มไปหน้าเข้าใช้งาน
    @FXML
    public void handleLoginButton(ActionEvent actionEvent){
        try {
            FXRouter.goTo("login-main");
        } catch (Exception err){
            System.out.println(err);
        }
    }

}
