package ku.cs.controller.employer.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.utility.CustomPopUp;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.util.HashMap;

public class OrderEstimatedLateDialogController {
    private Stage popupStage;

    static final CustomPopUp customPopUp = PopUpUtility.getPopUp("delete-confirmation");

    @FXML
    private TextField textField;

    @FXML
    private Button confirmButton;
    
    @FXML
    private Button cancelButton;

    @FXML
    private Label headerLabel;

    @FXML
    private Label objectLabel;

    @FXML void initialize() {
        ProjectUtility.debug(customPopUp.getPassingData());
        if (customPopUp.getPassingData() != null && customPopUp.getPassingData() instanceof HashMap) {
            HashMap<String, Object> passingData = (HashMap<String, Object>) customPopUp.getPassingData();
            if (!passingData.containsKey("headerLabel"))
                throw new RuntimeException("DeleteConfirmationDialogController: headerLabel is not found in passingData");
            if (!passingData.containsKey("objectLabel"))
                throw new RuntimeException("DeleteConfirmationDialogController: objectLabel is not found in passingData");
            headerLabel.setText((String) passingData.get("headerLabel"));
            objectLabel.setText((String) passingData.get("objectLabel"));
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


    // Add more logic as needed
}
