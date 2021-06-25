package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.Client;
import utils.ClientLauncher;
import utils.ObservableResourceFactory;

import java.io.IOException;


public class RegisterController {

    private Client client;
    private ClientLauncher clientLauncher;
    private Scene authScene;
    private ObservableResourceFactory resourceFactory;

    public void setAuthScene(Scene authScene) {
        this.authScene = authScene;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public void setClientLauncher(ClientLauncher clientLauncher){
        this.clientLauncher = clientLauncher;
    }

    @FXML
    private Label registrationTitle;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private TextField repeatPassword;

    @FXML
    private Button btn_register;

    @FXML
    private Label label_info;

    @FXML
    private Label to_auth;

    public void pressButton(ActionEvent actionEvent) throws IOException {
        String username = this.login.getText();
        String password = this.password.getText();
        String repeatPassword = this.repeatPassword.getText();
        if(repeatPassword.equals(password) &&
                username.length() > 0 && password.length() > 0 &&
                client.login("register", username, password)){
            label_info.setTextFill(Color.color(0,1,0));
            label_info.textProperty().bind(resourceFactory.getStringBinding("regLabelInfoGood"));
            label_info.setVisible(true);

            clientLauncher.setTableWindow();
        }
        else{
            label_info.setTextFill(Color.color(1,0,0));
            label_info.textProperty().bind(resourceFactory.getStringBinding("regLabelInfoWrong"));
            label_info.setText("Ошибка при регистрации");
            label_info.setVisible(true);
        }
    }

    public void clickToAuth(MouseEvent mouseEvent) throws IOException {
        //clientLauncher.changeScene("Login.fxml");
        clientLauncher.changeScene(authScene);
    }

    public void bindAppLanguage(){
        registrationTitle.textProperty().bind(resourceFactory.getStringBinding("registrationTitle"));
        login.promptTextProperty().bind(resourceFactory.getStringBinding("regLoginPromptText"));
        password.promptTextProperty().bind(resourceFactory.getStringBinding("regPasswordPromptText"));
        repeatPassword.promptTextProperty().bind(resourceFactory.getStringBinding("regDoublePasswordPromptText"));
        to_auth.textProperty().bind(resourceFactory.getStringBinding("authLink"));
        btn_register.textProperty().bind(resourceFactory.getStringBinding("registrationButton"));
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindAppLanguage();
    }

}
