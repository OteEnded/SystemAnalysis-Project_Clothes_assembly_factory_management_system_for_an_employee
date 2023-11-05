package ku.cs.controller.employer.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ku.cs.model.Work;
import ku.cs.utility.CustomPopUp;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class OrderEstimatedLateDialogController {
    private Stage popupStage;

    static final CustomPopUp customPopUp = PopUpUtility.getPopUp("order-estimated-late");

    private Work work;
    
    @FXML
    private Button okButton;

    @FXML
    private Label headerLabel;

    @FXML private Label workDeadLineLabel;
    @FXML private Label recommendGoalAmountLabel;
    @FXML private Label workGoalAmountLabel;
    @FXML private Label recommendDeadLineLabel;

    @FXML private HBox recommendGoalAmountHBox;

    @FXML void initialize() throws SQLException, ParseException {
        ProjectUtility.debug(customPopUp.getPassingData());
        if (customPopUp.getPassingData() != null && customPopUp.getPassingData() instanceof HashMap) {
            HashMap<String, Object> passingData = (HashMap<String, Object>) customPopUp.getPassingData();
            if (!passingData.containsKey("work")) throw new RuntimeException("DeleteConfirmationDialogController[initialize]: passingData does not contain key 'work'");
            work = (Work) passingData.get("work");
            workGoalAmountLabel.setText(work.getGoalAmount() + "");
            recommendDeadLineLabel.setText(work.getRecommendedDeadline().toString());
            if (work.getRecommendedGoalAmount() <= 0){
                recommendGoalAmountHBox.setVisible(false);
                return;
            }
            workDeadLineLabel.setText(work.getDeadline().toString());
            recommendGoalAmountLabel.setText(work.getRecommendedGoalAmount() + "");

            return;
        }
        throw new RuntimeException("DeleteConfirmationDialogController[initialize]: passingData is not valid");
    }

    @FXML
    public void onOkButtonClick() {
        customPopUp.setCloseBy(PopUpUtility.closeWith_cancel);
        customPopUp.close();
    }


    // Add more logic as needed
}
