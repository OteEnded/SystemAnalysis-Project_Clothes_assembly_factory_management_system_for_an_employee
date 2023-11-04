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
import java.time.LocalDate;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ProjectUtility {

    public static final int programHeight = 720;
    public static final int programWidth = 1280;

    public static final boolean isDebug = true;

    public static void debug(Object... msgs) {
        debug(Arrays.stream(msgs).toList(), true);
    }

    public static void debug(List<Object> msgs){
        debug(msgs, false);
    }

    public static void debug(List<Object> msgs, boolean asOneLine){
        if (asOneLine) {
            List<String> msgList = new ArrayList<>();
            for (Object i : msgs) {
                if (i == null) i = "null";
                msgList.add(i.toString());
            }
            debug(String.join(" ", msgList));
            return;
        }
        for (Object i: msgs) debug(i);
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
        return getDate(LocalDate.now());
    }

    public static Date getDate(String date){
        return Date.valueOf(date);
    }

    public static Date getDate(int year, int month, int day){
        return Date.valueOf(year + "-" + month + "-" + day);
    }

    public static Date getDate(String year, String month, String day){
        return Date.valueOf(year + "-" + month + "-" + day);
    }

    public static Date getDate(LocalDate localDate){
        return Date.valueOf(localDate);
    }

    public static int differanceDate(Date date1, Date date2){
        return differanceDate(date1.toLocalDate(), date2.toLocalDate());
    }

    public static int differanceDate(LocalDate date1, LocalDate date2){
        return (int) (date1.toEpochDay() - date2.toEpochDay());
    }

    public static Date getDateWithOffset(Date date, int offset){
        if (date == null) getDate(offset);
        return getDateWithOffset(date.toLocalDate(), offset);
    }

    public static Date getDateWithOffset(LocalDate date, int offset){
        if (date == null) getDate(offset);
        return Date.valueOf(date.plusDays(offset));
    }

    public static Date getDate(int offsetFromToday){
        return getDate(LocalDate.now().plusDays(offsetFromToday));
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

    public static Object castStringToObject(String stringValue, Class<?> targetClass) {
        if (targetClass == String.class) return stringValue;
        if (targetClass == Integer.class || targetClass == int.class) return Integer.parseInt(stringValue);
        if (targetClass == Long.class || targetClass == long.class) return Long.parseLong(stringValue);
        if (targetClass == Float.class || targetClass == float.class) return Float.parseFloat(stringValue);
        if (targetClass == Double.class || targetClass == double.class) return Double.parseDouble(stringValue);
        if (targetClass == Boolean.class || targetClass == boolean.class) return Boolean.parseBoolean(stringValue);
        if (targetClass == Date.class) return Date.valueOf(stringValue);
        if (targetClass == Timestamp.class) return Timestamp.valueOf(stringValue);
        throw new RuntimeException("ProjectUtility[castStringToObject]: Invalid targetClass -> " + targetClass);
    }


    private static Stage mainStage;
    public static void setMainStage(Stage s){
        mainStage = s;
    }
    public static Stage getMainStage(){
        return mainStage;
    }

    private static Object applicationReference;
    public static void setApplicationReference(Object ar){
        applicationReference = ar;
    }
    public static Object getApplicationReference(){
        return applicationReference;
    }

}