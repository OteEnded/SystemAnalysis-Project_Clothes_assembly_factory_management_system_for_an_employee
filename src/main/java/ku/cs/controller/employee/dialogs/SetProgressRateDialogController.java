package ku.cs.controller.employee.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.entity.Works;
import ku.cs.model.DailyRecord;
import ku.cs.model.Material;
import ku.cs.model.Work;
import ku.cs.utility.CustomPopUp;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SetProgressRateDialogController {
    private Stage popupStage;

    static final CustomPopUp customPopUp = PopUpUtility.getPopUp("set-progress-rate");

    @FXML
    private TextField textField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML private Label objectLabel;
    private Work work;


    @FXML void initialize() {
        ProjectUtility.debug(customPopUp.getPassingData());
        if (customPopUp.getPassingData() != null && customPopUp.getPassingData() instanceof HashMap) {
            HashMap<String, Object> passingData = (HashMap<String, Object>) customPopUp.getPassingData();
            if (!passingData.containsKey("objectLabel"))
                throw new RuntimeException("DeleteConfirmationDialogController: objectLabel is not found in passingData");
            objectLabel.setText((String) passingData.get("objectLabel"));
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
    public void onConfirmButtonClick() throws SQLException, IOException {
        customPopUp.setCloseBy(PopUpUtility.closeWith_confirm);
        handleEstimate();
        customPopUp.close();
    }

    @FXML
    public void onCancelButtonClick() {
        customPopUp.setCloseBy(PopUpUtility.closeWith_cancel);
        customPopUp.close();
    }

    private void handleEstimate() throws SQLException, IOException {
        String userEstimateProgressRateStr = textField.getText();
        boolean checkNumeric = Pattern.matches("[0-9]+", userEstimateProgressRateStr);
        if(checkNumeric) {
            int userEstimateProgressRate = Integer.parseInt(userEstimateProgressRateStr);
            work.getProduct().setProgressRate(userEstimateProgressRate);
            if(work.getEstimated().equals(Works.estimate_late)) {
                HashMap<String, Object> passingData = new HashMap<>();
                passingData.put("work", work);
                PopUpUtility.popUp("progress-rate-warning", passingData);
                if (!PopUpUtility.getPopUp("progress-rate-warning").isPositiveClosing()) return;
            }
        } else {
            System.err.println("invalid");
        }
    }
}
