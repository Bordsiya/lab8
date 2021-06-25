package exceptions;

public class UserIsAlreadyRegisteredException extends Exception{

    public UserIsAlreadyRegisteredException(String message){
        super(message);
    }

}
