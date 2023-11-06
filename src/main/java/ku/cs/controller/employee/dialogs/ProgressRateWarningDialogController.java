package ku.cs.controller.employee.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.model.Work;
import ku.cs.utility.CustomPopUp;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class ProgressRateWarningDialogController {
    private Stage popupStage;

    static final CustomPopUp customPopUp = PopUpUtility.getPopUp("progress-rate-warning");

    @FXML
    private TextField textField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    public Label recommendedProgressRate;

    private Work work;

    @FXML void initialize() throws SQLException, ParseException {
        ProjectUtility.debug(customPopUp.getPassingData());
        if (customPopUp.getPassingData() != null && customPopUp.getPassingData() instanceof HashMap) {
            HashMap<String, Object> passingData = (HashMap<String, Object>) customPopUp.getPassingData();
            work = (Work) passingData.get("work");
//            recommendedProgressRate.setText(String.valueOf(work.getRecommendedProgressRate()));
        }
        else {
            throw new RuntimeException("DeleteConfirmationDialogController[initialize]: passingData is not valid");
        }
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    public void closePopup() {
        popupStage.close();
    }

    @FXML
    public void onOkButtonClick() {
        customPopUp.setCloseBy(PopUpUtility.closeWith_ok);
        customPopUp.close();
    }

    @FXML
    public void onNoButtonClick() {
        customPopUp.setCloseBy(PopUpUtility.closeWith_no);
        customPopUp.close();
    }

}
