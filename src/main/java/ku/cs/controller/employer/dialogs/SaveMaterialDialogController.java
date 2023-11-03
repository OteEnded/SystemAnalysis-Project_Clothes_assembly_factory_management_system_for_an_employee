package ku.cs.controller.employer.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.model.Material;
import ku.cs.utility.CustomPopUp;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.util.HashMap;

public class SaveMaterialDialogController {

    private Stage popupStage;

    static final CustomPopUp customPopUp = PopUpUtility.getPopUp("save-material");

    private Material material;

    @FXML
    private Label promptLabel;

    @FXML
    private TextField materialName;

    @FXML
    private TextField materialUnitName;

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
            if (!passingData.containsKey("material")){
                headerLabel.setText("เพิ่มวัตถุดิบ");
                material = new Material();
            }
            else {
                headerLabel.setText("แก้ไขวัตถุดิบ");
                material = (Material) passingData.get("material");
                materialName.setText(material.getName());
                materialUnitName.setText(material.getUnitName());
            }
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

    private boolean validateInput(){
        if (materialName.getText().isEmpty()){
            promptLabel.setText("กรุณากรอกชื่อวัตถุดิบ");
            return false;
        }
        if (materialUnitName.getText().isEmpty()){
            promptLabel.setText("กรุณากรอกหน่วยของวัตถุดิบ");
            return false;
        }
        return true;
    }

    @FXML
    public void onConfirmButtonClick() {

        if (!validateInput()) return;

        material.setName(materialName.getText());
        material.setUnitName(materialUnitName.getText());
        customPopUp.setPassingData(material);

        customPopUp.setCloseBy(PopUpUtility.closeWith_confirm);
        customPopUp.close();
    }

    @FXML
    public void onCancelButtonClick() {
        customPopUp.setCloseBy(PopUpUtility.closeWith_cancel);
        customPopUp.close();
    }
}
