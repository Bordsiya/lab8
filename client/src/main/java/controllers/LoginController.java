package controllers;

import animations.Shake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.Client;
import utils.ClientLauncher;
import utils.ObservableResourceFactory;

import java.io.IOException;

public class LoginController {

    private Client client;
    private ClientLauncher clientLauncher;
    private ObservableResourceFactory resourceFactory;
    private Scene registerScene;

    public void setClient(Client client){
        this.client = client;
    }
    public void setClientLauncher(ClientLauncher clientLauncher){
        this.clientLauncher = clientLauncher;
    }

    public void setRegisterScene(Scene registerScene) {
        this.registerScene = registerScene;
    }

    @FXML
    private Label authTitle;

    @FXML
    private Button btn_auth;

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private Label label_info;

    @FXML
    private Label to_register;

    public void pressButton(ActionEvent actionEvent) throws IOException {
        String username = login.getText();
        String passwordField = this.password.getText();
        if(client.login("login", username, passwordField)){
            label_info.setTextFill(Color.color(0,1,0));
            label_info.textProperty().bind(resourceFactory.getStringBinding("authLabelInfoGood"));
            label_info.setVisible(true);
            clientLauncher.setTableWindow();
        }
        else{
            label_info.setTextFill(Color.color(1,0,0));
            label_info.textProperty().bind(resourceFactory.getStringBinding("authLabelInfoWrong"));
            label_info.setVisible(true);
            Shake loginAnimation = new Shake(login);
            Shake passwordAnimation = new Shake(password);
            loginAnimation.playAnimation();
            passwordAnimation.playAnimation();
        }
    }

    public void clickToLogin(MouseEvent mouseEvent) throws IOException {
        clientLauncher.changeScene(registerScene);
    }

    public void bindAppLanguage(){
        authTitle.textProperty().bind(resourceFactory.getStringBinding("authTitle"));
        login.promptTextProperty().bind(resourceFactory.getStringBinding("authLoginPromptText"));
        password.promptTextProperty().bind(resourceFactory.getStringBinding("authPasswordPromptText"));
        to_register.textProperty().bind(resourceFactory.getStringBinding("registrationLink"));
        btn_auth.textProperty().bind(resourceFactory.getStringBinding("authorizationButton"));
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindAppLanguage();
    }

}
