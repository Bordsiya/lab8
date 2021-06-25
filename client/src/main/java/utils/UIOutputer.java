package utils;

import javafx.scene.control.Alert;

import java.text.MessageFormat;

public class UIOutputer {

    public static final String INFO_TITLE = "informationWindowTitle";
    public static final String ERROR_TITLE = "exceptionTitle";
    private static ObservableResourceFactory resourceFactory;

    public static void setResourceFactory(ObservableResourceFactory resourceFactory) {
        UIOutputer.resourceFactory = resourceFactory;
    }

    public static void makeAlert(Alert.AlertType alertType, String title, String [] args, String text){
        Alert alert = new Alert(alertType);
        alert.setTitle(resourceFactory.getResources().getString(title));
        alert.setHeaderText(null);
        alert.setContentText(checkRes(text, args));
        alert.showAndWait();
    }

    public static void infoAlert(String text, String[] args){
        makeAlert(Alert.AlertType.INFORMATION, INFO_TITLE, args, text);
    }

    public static void infoAlert(String text){
        makeAlert(Alert.AlertType.INFORMATION, INFO_TITLE, null, text);
    }

    public static void errorAlert(String text, String[] args){
        makeAlert(Alert.AlertType.ERROR, ERROR_TITLE, args, text);
    }

    public static void errorAlert(String text){
        makeAlert(Alert.AlertType.ERROR, ERROR_TITLE, null, text);
    }

    public static void chooseAlertType(String text, String[] args){
        if(text.startsWith("Error:")){
            makeAlert(Alert.AlertType.ERROR, ERROR_TITLE, args, text);
        }
        else makeAlert(Alert.AlertType.INFORMATION, INFO_TITLE, args, text);
    }

    public static void chooseAlertType(String text){
        chooseAlertType(text, null);
    }

    public static String checkRes(String str, String[] args){
        if(args == null) return resourceFactory.getResources().getString(str);
        String text1 = resourceFactory.getResources().getString(str.replace("\n", ""));
        if(str.equals("helpAnswer\n")){
            text1 += "\n";
            for(int i = 0; i < args.length; i+=2){
                text1 += args[i] + " - " + resourceFactory.getResources().getString(args[i+1]) + "\n";
            }
        }
        else if(str.equals("infoAnswer\n")){
            text1 += "\n";
            text1 += resourceFactory.getResources().getString(args[0]) + " : " + args[1] + "\n";
            text1 += resourceFactory.getResources().getString(args[2]) + " : " + args[3] + "\n";
        }
        else{
            for(String arg : args){
                text1 += "\n" + arg;
            }
        }
        return text1;
    }




}
