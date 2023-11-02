package ku.cs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.utility.CustomPopUp;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

public class LoadingDialogController {
    private Stage popupStage;

    static final CustomPopUp customPopUp = PopUpUtility.getPopUp("loading");

    @FXML
    private TextField textField;

    @FXML
    private Button okButton;

    @FXML void initialize() {
        ProjectUtility.debug(customPopUp.getPassingData());
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    public void closePopup() {
        popupStage.close();
    }

    @FXML
    public void onOKButtonClick() {
        // Get the input data from the dialog
//        String userInput = textField.getText(); // Example data retrieval

        // Perform some processing with the input

        customPopUp.setCloseBy(PopUpUtility.closeWith_ok);

        // Close the dialog
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }


    // Add more logic as needed
}
