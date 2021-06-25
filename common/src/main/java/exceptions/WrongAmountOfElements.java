package exceptions;

/**
 * Exception for calling a command with an invalid number of arguments
 * @author NastyaBordun
 * @version 1.1
 */

public class WrongAmountOfElements extends Exception{

    public WrongAmountOfElements(String message){
        super(message);
    }

}
