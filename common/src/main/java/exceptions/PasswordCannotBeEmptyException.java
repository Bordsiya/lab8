package exceptions;

public class PasswordCannotBeEmptyException extends Exception{

    public PasswordCannotBeEmptyException(String message){
        super(message);
    }

}
