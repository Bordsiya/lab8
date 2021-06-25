package utils;

import controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Class for launching Client
 * @author NastyaBordun
 * @version 1.1
 */
public class ClientLauncher extends Application {
    /**
     * Port for connection
     */
    public static final int PORT = 1246;
    public static Stage mainStage;
    public static final String BUNDLE = "properties.app";
    public static final String APP_TITLE = "App";
    private static Client client;
    private static Business business;
    private static ObservableResourceFactory resourceFactory;

    public void setTableWindow() {
        try {
            FXMLLoader tableWindowLoader = new FXMLLoader();
            tableWindowLoader.setLocation(getClass().getResource("/scenes/Table.fxml"));
            Parent mainWindowRootNode = tableWindowLoader.load();
            Scene mainWindowScene = new Scene(mainWindowRootNode);
            TableController tableController = tableWindowLoader.getController();

            tableController.setClient(client);
            tableController.setBusiness(business);
            tableController.initializeAll();
            tableController.initLangs(resourceFactory);

            FXMLLoader EditSpaceShipLoader = new FXMLLoader();
            EditSpaceShipLoader.setLocation(getClass().getResource("/scenes/EditSpaceShip.fxml"));
            Parent editWindowRootNode = EditSpaceShipLoader.load();
            Scene editWindowScene = new Scene(editWindowRootNode);
            Stage editStage = new Stage();
            editStage.setTitle(APP_TITLE);
            editStage.setScene(editWindowScene);
            editStage.setResizable(false);
            editStage.initModality(Modality.WINDOW_MODAL);
            editStage.initOwner(mainStage);
            EditSpaceShipController editSpaceShipController = EditSpaceShipLoader.getController();
            editSpaceShipController.setEditSpaceShipStage(editStage);
            editSpaceShipController.setBusiness(business);
            editSpaceShipController.initLangs(resourceFactory);

            tableController.setEditStage(editStage);
            tableController.setEditSpaceShipController(editSpaceShipController);

            FXMLLoader infoSpaceShipLoader = new FXMLLoader();
            infoSpaceShipLoader.setLocation(getClass().getResource("/scenes/Information.fxml"));
            Parent infoWindowRootNode = infoSpaceShipLoader.load();
            Scene infoWindowScene = new Scene(infoWindowRootNode);
            Stage infoStage = new Stage();
            infoStage.setTitle(APP_TITLE);
            infoStage.setScene(infoWindowScene);
            infoStage.setResizable(false);
            infoStage.initModality(Modality.WINDOW_MODAL);
            infoStage.initOwner(mainStage);
            InfoSpaceShipController infoSpaceShipController = infoSpaceShipLoader.getController();
            infoSpaceShipController.initLangs(resourceFactory);

            tableController.setInfoStage(infoStage);
            tableController.setInfoSpaceShipController(infoSpaceShipController);

            mainStage.setScene(mainWindowScene);
            mainStage.setTitle(APP_TITLE);
            mainStage.getIcons().add(new Image("/pictures/icon.png"));
            mainStage.setResizable(false);
        } catch (IOException exception) {
            UIOutputer.errorAlert("ioeException");
        }
    }

    /**
     * Launching Client
     */
    public void launchClient(String[] args){
        BufferedInputStream bf = new BufferedInputStream(System.in);
        BufferedReader r = new BufferedReader(new InputStreamReader(bf, StandardCharsets.UTF_8));
        AskManager askManager = new AskManager(r);
        AuthorizationBusiness authorizationBusiness = new AuthorizationBusiness();
        business = new Business();
        try {
            InetAddress ADDR = InetAddress.getByName("localhost");
            DatagramSocket datagramSocket1 = new DatagramSocket();
            Receiver receiverCommand = new Receiver(datagramSocket1);
            Sender senderCommand = new Sender(datagramSocket1, ADDR);
            DatagramSocket datagramSocket2 = new DatagramSocket();
            Receiver receiverRefresh = new Receiver(datagramSocket2);
            Sender senderRefresh = new Sender(datagramSocket2, ADDR);
            BusinessScript businessScript = new BusinessScript(askManager);

            client = new Client(receiverCommand, senderCommand,
                    authorizationBusiness, businessScript,
                    receiverRefresh, senderRefresh);
            ScriptConsole scriptConsole = new ScriptConsole(client, askManager);
            businessScript.addConsole(scriptConsole);

            resourceFactory = new ObservableResourceFactory();
            resourceFactory.setResources(ResourceBundle.getBundle(BUNDLE));

            launch(args);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            Printer.println("Ошибка во время создания клиента");
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;

        mainStage.getIcons().add(new Image("/pictures/icon.png"));

        FXMLLoader loginWindowLoader = new FXMLLoader();
        loginWindowLoader.setLocation(getClass().getResource("/scenes/Login.fxml"));
        Parent loginWindowRootNode = loginWindowLoader.load();
        Scene loginWindowScene = new Scene(loginWindowRootNode);
        Stage authStage = new Stage();
        authStage.setTitle(APP_TITLE);
        authStage.setScene(loginWindowScene);
        authStage.setResizable(false);
        authStage.initModality(Modality.NONE);
        authStage.initOwner(mainStage);
        LoginController loginController = loginWindowLoader.getController();
        loginController.setClient(client);
        loginController.setClientLauncher(this);


        FXMLLoader registerWindowLoader = new FXMLLoader();
        registerWindowLoader.setLocation(getClass().getResource("/scenes/Register.fxml"));
        Parent registerWindowRootNode = registerWindowLoader.load();
        Scene registerWindowScene = new Scene(registerWindowRootNode);
        RegisterController registerController = registerWindowLoader.getController();
        registerController.setClient(client);
        registerController.setClientLauncher(this);
        registerController.initLangs(resourceFactory);
        registerController.setAuthScene(loginWindowScene);

        loginController.initLangs(resourceFactory);
        loginController.setRegisterScene(registerWindowScene);

        UIOutputer.setResourceFactory(resourceFactory);

        mainStage.setScene(loginWindowScene);
        mainStage.setTitle(APP_TITLE);
        mainStage.setResizable(true);
        mainStage.show();
    }

    public void changeScene(Scene scene){
        mainStage.setScene(scene);
    }
}
