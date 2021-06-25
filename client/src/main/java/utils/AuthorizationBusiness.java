package utils;

import answers.Request;
import data.User;

import java.io.BufferedReader;

public class AuthorizationBusiness {

    public Request makeAuthRequest(String username, String password){
        User user = new User(username, password);
        return new Request("login", user);
    }

    public Request makeRegisterRequest(String username, String password){
        User user = new User(username, password);
        return new Request("register", user);
    }

}
