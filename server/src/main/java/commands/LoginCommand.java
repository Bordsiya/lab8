package commands;

import data.User;
import exceptions.UserNotFoundException;
import exceptions.WrongAmountOfElements;
import utils.ResponseBuilder;
import utils.database.DatabaseUserManager;

import java.sql.SQLException;

/**
 * Login command
 * @author NastyaBordun
 * @version 1.1
 */

public class LoginCommand extends AbstractCommand{

    private CommandBase commandBase;

    private DatabaseUserManager databaseUserManager;

    public LoginCommand(CommandBase commandBase, DatabaseUserManager databaseUserManager){
        super("login", "loginDescription");
        this.commandBase = commandBase;
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public boolean execute(String str, Object arg, User user) {
        try {
            //commandBase.login();
            if (str.length() != 0 || arg != null)
                throw new WrongAmountOfElements("wrongAmountOfElements");
            if(databaseUserManager.checkUserByUsernameAndPassword(user)){
                ResponseBuilder.appendln("userAlreadyExistException");
            }
            else throw new UserNotFoundException("userNotFoundException");
            return true;
        }
        catch (WrongAmountOfElements | UserNotFoundException e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }
        catch (SQLException e){
            ResponseBuilder.appendError("errorWithDbException");
            return false;
        }
    }

}
