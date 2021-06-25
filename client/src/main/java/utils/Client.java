package utils;

import answers.Request;
import answers.Response;
import answers.ResponseAnswer;
import data.User;
import exceptions.IncorrectCommandException;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Client
 * @author NastyaBordun
 * @version 1.1
 */
public class Client {
    /**
     * Class for receiving responses
     */
    private Receiver receiverCommand;
    /**
     * Class for sending requests
     */
    private Sender senderCommand;

    private Receiver receiverRefresh;

    private Sender senderRefresh;

    private BusinessScript businessScript;

    private AuthorizationBusiness authorizationBusiness;

    private User user;

    private Color userColor = Color.rgb(153, 255, 255);

    /**
     * Constructor for class
     */
    public Client(Receiver receiverCommand, Sender senderCommand,
                  AuthorizationBusiness authorizationBusiness,
                  BusinessScript businessScript,
                  Receiver receiverRefresh, Sender senderRefresh){
        this.receiverCommand = receiverCommand;
        this.senderCommand = senderCommand;
        this.receiverRefresh = receiverRefresh;
        this.senderRefresh = senderRefresh;
        this.authorizationBusiness = authorizationBusiness;
        this.businessScript = businessScript;
    }

    public User getUser(){
        return this.user;
    }

    public Color getUserColor(){
        return this.userColor;
    }

    /**
     * Interaction with Server
     * @param command command from Console
     */
    public void handle(String command, String commandStringArg, Serializable commandObjectArg, User user){
        Request request = new Request(command, commandStringArg, commandObjectArg, user);
        if(senderCommand.send(request, ClientLauncher.PORT)){
            Response response = receiverCommand.getResponse();
            if(response != null){
                //System.out.println(response);
                UIOutputer.chooseAlertType(response.getResponseBody(), response.getResponseArgs());
            }
        }
    }

    public Response handleRefresh(String command, String commandStringArg, Serializable commandObjectArg, User user){
        Request request = new Request(command, commandStringArg, commandObjectArg, user);
        if(senderRefresh.send(request, ClientLauncher.PORT)){
            return receiverRefresh.getResponse();
        }
        return null;
    }

    public boolean login(String command, String username, String password){
        Request request;
        if(command.equals("login")) request = authorizationBusiness.makeAuthRequest(username, password);
        else request = authorizationBusiness.makeRegisterRequest(username, password);

        if(request != null){
            if(senderCommand.send(request, ClientLauncher.PORT)){
                Response response = receiverCommand.getResponse();
                if(response != null && response.getResponseAnswer().equals(ResponseAnswer.OK)){
                    Printer.println("Вы авторизованы. Можете приступать к работе.");
                    this.user = request.getUser();
                    this.userColor = Color.color((float)Math.random(), (float)Math.random(), (float)Math.random());
                    return true;
                }
                else{
                    Printer.println("Вы не авторизованы. Попробуйте еще.");
                    return false;
                }
            }
            else return false;
        }
        else return false;
    }

    public void handleScript(String command){
        try {
            Request request = businessScript.makeRequest(command, this.user);

            if(request != null){
                if(senderCommand.send(request, ClientLauncher.PORT)){
                    Response response = receiverCommand.getResponse();
                    if(response != null) {
                        UIOutputer.chooseAlertType(response.getResponseBody(), response.getResponseArgs());
                    }
                }
            }

        } catch (IncorrectCommandException e) {
            UIOutputer.errorAlert("incorrectCommandException");
            Printer.printError(e.getMessage());
        }
    }


}
