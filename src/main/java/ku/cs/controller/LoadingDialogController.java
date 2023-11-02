package ku.cs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ku.cs.utility.CustomPopUp;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

public class LoadingDialogController {
    private Stage popupStage;

    static final CustomPopUp customPopUp = PopUpUtility.getPopUp("loading");

    @FXML
    private Label loadingEntityLabel;

    @FXML
    private Button closeButton;

    @FXML void initialize() {
        loadingEntityLabel.setText((String) customPopUp.getPassingData());
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    public void closePopup() {
        popupStage.close();
    }

    @FXML
    public void onCloseButtonClick() {
        // Get the input data from the dialog
//        String userInput = textField.getText(); // Example data retrieval

        // Perform some processing with the input

        customPopUp.setCloseBy(PopUpUtility.closeWith_close);

        // Close the dialog
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


    // Add more logic as needed
}
