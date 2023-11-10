package ku.cs.controller.employee.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.entity.Works;
import ku.cs.model.DailyRecord;
import ku.cs.model.Material;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.utility.CustomPopUp;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
    @FXML private Label promptLabel;

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
    public void onConfirmButtonClick() throws SQLException, IOException, ParseException {
        customPopUp.setCloseBy(PopUpUtility.closeWith_confirm);

        String userEstimateProgressRateStr = textField.getText();
        boolean checkNumeric = Pattern.matches("[0-9]+", userEstimateProgressRateStr);
        if(checkNumeric) {
            if(Long.parseLong(textField.getText()) > Integer.MAX_VALUE) {
                promptLabel.setText("กรุณากรอกตัวเลขให้ถูกต้อง");
                return;
            }
            if(Integer.parseInt(textField.getText()) == 0){
                promptLabel.setText("กรุณากรอกตัวเลขให้ถูกต้อง");
                return;
            }
            promptLabel.setText("");
            int userEstimateProgressRate = Integer.parseInt(userEstimateProgressRateStr);
            Product product = work.getProduct();
            ProjectUtility.debug("######################",work.getRecommendedProgressRate());
            if(userEstimateProgressRate < work.getRecommendedProgressRate()){
                HashMap<String, Object> passingData = new HashMap<>();
                passingData.put("work", work);
                PopUpUtility.popUp("progress-rate-warning", passingData);
                if (PopUpUtility.getPopUp("progress-rate-warning").isPositiveClosing()) return;
            }
            product.setProgressRate(userEstimateProgressRate);
            product.save();
        } else {
            System.err.println("invalid");
            promptLabel.setText("กรุณากรอกตัวเลขเท่านั้น");
            return;
        }


        customPopUp.close();
    }

    @FXML
    public void onCancelButtonClick() {
        customPopUp.setCloseBy(PopUpUtility.closeWith_cancel);
        customPopUp.close();
    }
}
