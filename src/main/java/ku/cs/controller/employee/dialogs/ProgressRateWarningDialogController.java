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

    private Work work;

    @FXML void initialize() {
        ProjectUtility.debug(customPopUp.getPassingData());
        if (customPopUp.getPassingData() != null && customPopUp.getPassingData() instanceof HashMap) {
            HashMap<String, Object> passingData = (HashMap<String, Object>) customPopUp.getPassingData();
            work = (Work) passingData.get("work");
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
    public void onConfirmButtonClick() {
        customPopUp.setCloseBy(PopUpUtility.closeWith_confirm);
        customPopUp.close();
    }

    @FXML
    public void onCancelButtonClick() {
        customPopUp.setCloseBy(PopUpUtility.closeWith_cancel);
        customPopUp.close();
    }

}
