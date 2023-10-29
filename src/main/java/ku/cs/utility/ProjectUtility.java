package ku.cs.utility;

import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ProjectUtility {

    public static final int programHeight = 720;
    public static final int programWidth = 1280;

    public static final boolean isDebug = true;

    public static void debug(Object... msgs) {
        debug(Arrays.stream(msgs).toList());
    }

    public static void debug(List<Object> msgs){
        List<String> msgList = new ArrayList<>();
        for (Object i: msgs) {
            if (i == null) i = "null";
            msgList.add(i.toString());
        }
        debug(String.join(" ", msgList));
    }

    public static void debug(Object msg){
        if (!isDebug) return;
        System.out.print("[DEBUG @ " + getDateTime().toString() + "]: ");
        if (msg == null) msg = "null";
        System.out.println(msg.toString());
    }

    /* https://alfatoxin.github.io/Code4Sec/Java/ */
    public static String hashString(String str){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {}
        byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public static String getDataBaseDirName(){
        if (isDebug) return "data-test";
        return "data";
    }

    public static String getDateTime(){
        return (new Timestamp(System.currentTimeMillis())).toString();
    }

    public static String getDateTime(String format){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timestamp);
    }

    public static Date getDate(){
        return getDate(null);
    }

    public static Date getDate(String date){
        if (date == null) return new Date(System.currentTimeMillis());
        return Date.valueOf(date);
    }

    public static void copyFile(String from, String to){
        File file = new File(from);
        Path path = Paths.get(to);
        try {
            Files.copy(file.toPath(), path,REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File pictureChooser(){
        ArrayList<String> fileExtensionList = new ArrayList<>();
        fileExtensionList.add("png");
        fileExtensionList.add("jpg");
        fileExtensionList.add("jpeg");
        HashMap<String, ArrayList<String>> fileType = new HashMap<>();
        fileType.put("Image", fileExtensionList);
        return fileChooser(fileType);
    }

    public static File fileChooser(){
        ArrayList<String> fileExtensionList = new ArrayList<>();
        fileExtensionList.add("*");
        HashMap<String, ArrayList<String>> fileType = new HashMap<>();
        fileType.put("All Files", fileExtensionList);
        return fileChooser(fileType);
    }

    public static File fileChooser(HashMap<String, ArrayList<String>> fileType){
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file.");

        ArrayList<ExtensionFilter> fileFilter = new ArrayList<>();
        for (String i: fileType.keySet()) {
            ArrayList<String> modifiedFileExtension = new ArrayList<>();
            for (String j: fileType.get(i)) modifiedFileExtension.add("*." + j);
            fileType.put(i, modifiedFileExtension);
        }
        for (String i: fileType.keySet()) fileFilter.add(new ExtensionFilter(i, fileType.get(i)));
        fileChooser.getExtensionFilters().addAll(fileFilter);

        return fileChooser.showOpenDialog(stage); //return selected file
    }

    public static File dirChooser(){
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory.");
        //directoryChooser.setInitialDirectory(new File(File.separator));

        File selectedDir = directoryChooser.showDialog(stage);
        return selectedDir;
    }

    public static Image getProgramIcon(){
        return new Image(ProjectUtility.class.getResource("/ku/cs/data/images/icon.png").toExternalForm());
    }

    public static boolean connectDB(){
        boolean isConnectAble = JdbcConnector.connect();
        JdbcConnector.disconnect();
        return isConnectAble;
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        if (str.length() == 1) return str.toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static Stage stage;
    public static void setStage(Stage s){
        stage = s;
    }
    public static Stage getStage(){
        return stage;
    }

}