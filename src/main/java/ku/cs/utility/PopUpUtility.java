package ku.cs.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PopUpUtility {

    public static final String closeWith_exit = "closeWith_exit";
    public static final String closeWith_confirm = "closeWith_confirm";
    public static final String closeWith_cancel = "closeWith_cancel";
    public static final String closeWith_ok = "closeWith_ok";
    public static final String closeWith_yes = "closeWith_yes";
    public static final String closeWith_no = "closeWith_no";
    public static final String closeWith_close = "closeWith_close";

    private static final HashMap<String, CustomPopUp> popUps = new HashMap<>();

    public static HashMap<String, CustomPopUp> getPopUps(){
        return popUps;
    }

    public static void addPopUp(CustomPopUp customPopUp) {
        popUps.put(customPopUp.getKey(), customPopUp);
    }

    public static void addPopUp(String key, CustomPopUp customPopUp) {
        popUps.put(key, customPopUp);
    }

    public static CustomPopUp getPopUp(String key) {
        return popUps.get(key);
    }

    public static String getPopUpPath(String key) {
        return popUps.get(key).getPath();
    }

    private static List<CustomPopUp> poppingUpList = new ArrayList<>();

    static void addPoppingUp(CustomPopUp customPopUp) {
        poppingUpList.add(customPopUp);
    }

    static void removePoppingUp(CustomPopUp customPopUp) {
        poppingUpList.remove(customPopUp);
    }


    public static void popUp(String key) throws IOException {
        popUp(getPopUp(key));
    }

    public static void popUp(CustomPopUp customPopUp) throws IOException {
        popUps.get(customPopUp.getKey()).popUp();
    }

    public static void popUp(String key, Object passData) throws IOException {
        popUp(getPopUp(key), passData);
    }

    public static void popUp(CustomPopUp customPopUp, Object passData) throws IOException {
        popUps.get(customPopUp.getKey()).popUp(passData);
    }

    public static void close(String key) {
        close(popUps.get(key));
    }

    public static void close(CustomPopUp customPopUp) {
        customPopUp.close();
    }

    public static void close(String key, boolean clearPassingData) {
        close(popUps.get(key), clearPassingData);
    }

    public static void close(CustomPopUp customPopUp, boolean clearPassingData) {
        customPopUp.close(clearPassingData);
    }
}
