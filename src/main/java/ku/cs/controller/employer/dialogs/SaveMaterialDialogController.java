package ku.cs.controller.employer.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.utility.PopUpUtility;

public class SaveMaterialDialogController {


    static final String popUpKey = "";

    @FXML
    private TextField textField;

    @FXML
    private Button okButton;

    @FXML
    public void onOKButtonClick() {
        // Get the input data from the dialog
//        String userInput = textField.getText(); // Example data retrieval

        // Perform some processing with the input

        PopUpUtility.getPopUp(popUpKey).setCloseBy(PopUpUtility.closeWith_ok);

        // Close the dialog
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }


    // Add more logic as needed
}
