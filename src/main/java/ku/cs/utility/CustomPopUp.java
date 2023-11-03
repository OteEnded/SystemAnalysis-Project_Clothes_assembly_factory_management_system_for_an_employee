package ku.cs.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.ProjectApplication;

import java.io.IOException;
import java.util.HashMap;

public class CustomPopUp {

    private String key;
    private String path;
    private String windowTitle;
    private String closeBy;
    private Object passingData;

    private Stage dialogStage;

    private boolean positiveClosing = true;

    private static HashMap<String, Boolean> isPositiveClosing = new HashMap<>();
    static {
        isPositiveClosing.put(PopUpUtility.closeWith_confirm, true);
        isPositiveClosing.put(PopUpUtility.closeWith_exit, false);
        isPositiveClosing.put(PopUpUtility.closeWith_cancel, false);
        isPositiveClosing.put(PopUpUtility.closeWith_ok, true);
        isPositiveClosing.put(PopUpUtility.closeWith_yes, true);
        isPositiveClosing.put(PopUpUtility.closeWith_no, false);
        isPositiveClosing.put(PopUpUtility.closeWith_close, true);
    }

    public CustomPopUp(){
    }

    public CustomPopUp(String key, String path, String windowTitle){
        this.key = key;
        this.path = path;
        this.windowTitle = windowTitle;
    }

    public CustomPopUp(String key, String path) {
        this.key = key;
        this.path = path;
    }

    public void setKey(String key){
        this.key = key;
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setWindowTitle(String windowTitle){
        this.windowTitle = windowTitle;
    }

    public String getKey(){
        return key;
    }

    public String getPath(){
        return path;
    }

    public String getWindowTitle(){
        return windowTitle;
    }

    public void setCloseBy(String closeBy){
        this.closeBy = closeBy;
    }


    public String getCloseBy(){
        return closeBy;
    }

    public void setPassingData(Object passingData){
        this.passingData = passingData;
    }

    public Object getPassingData(){
        return passingData;
    }

    public void clearData(){
        passingData = null;
        closeBy = null;
        windowTitle = null;
    }

    public boolean isPositiveClosing() {
        return positiveClosing;
    }

    public void popUp(Object passingData) throws IOException {

        if (passingData instanceof HashMap) {
            HashMap<String, Object> passingDataHashMap = (HashMap<String, Object>) passingData;
            if (passingDataHashMap.containsKey("windowTitle")) windowTitle = (String) passingDataHashMap.get("windowTitle");
            if (passingDataHashMap.containsKey("passingData")) passingData = passingDataHashMap.get("passingData");
        }

        this.passingData = passingData;
        popUp();
    }

    public void popUp() throws IOException {
        ProjectUtility.debug("CustomPopUp[popUp]: popping up popUp ->", key);

        PopUpUtility.addPoppingUp(this);

        FXMLLoader loader = new FXMLLoader(ProjectApplication.class.getResource(path));
        Parent dialog;
        try{
            dialog = loader.load();
        }
        catch (Exception e){
            ProjectUtility.debug("#### CustomPopUp[popUp]: error loading popUp ->", key);
            ProjectUtility.debug("#### CustomPopUp[popUp]: please check if the path or fxml file is correct ->", path);
            throw new RuntimeException(e);
        }
        if (dialog == null) throw new RuntimeException("CustomPopUp[popUp]: dialog is null");

        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        if (key.equals("loading")) windowTitle = "กำลังโหลด..." + passingData;

        if (passingData != null && passingData instanceof HashMap) {
            HashMap<String, Object> passingDataMap = (HashMap<String, Object>) passingData;
            if (passingDataMap.containsKey("windowsTitle")) windowTitle = ((String) passingDataMap.get("windowsTitle"));
        }

        if (windowTitle == null) windowTitle = "โปรดทราบ...";
        dialogStage.setTitle(windowTitle);
        dialogStage.setScene(new Scene(dialog));

        if (key.equals("loading")) dialogStage.show();
        else dialogStage.showAndWait();

        if (closeBy == null) closeBy = PopUpUtility.closeWith_exit;

        positiveClosing = isPositiveClosing.get(closeBy);
        PopUpUtility.removePoppingUp(this);
        ProjectUtility.debug("CustomPopUp[popUp]: closed popUp ->", key, "->", closeBy);
    }

    public void close(){
        close(key.equals("loading"));
    }

    public void close(boolean clearPassingData){
        ProjectUtility.debug("CustomPopUp[close]: closing popUp ->", key);
        if (clearPassingData) clearData();
        dialogStage.close();
        if (closeBy == null) closeBy = PopUpUtility.closeWith_close;
        ProjectUtility.debug("CustomPopUp[close]: closed popUp ->", key);
    }


    @Override
    public String toString() {
        return "CustomPopUp{" +
                "key='" + key + '\'' +
                ", path='" + path + '\'' +
                ", windowTitle='" + windowTitle + '\'' +
                '}';
    }
}
