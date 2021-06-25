package commands;

import data.User;
import exceptions.WrongAmountOfElements;
import utils.ResponseBuilder;

/**
 * Command for script executing from the certain file
 * @author NastyaBordun
 * @version 1.1
 */

public class ExecuteScriptCommand extends AbstractCommand{
    /**
     * Base for all commands {@link CommandBase}
     */
    private CommandBase commandBase;

    /**
     * Constructor for the command
     * @param commandBase base for commands
     */
    public ExecuteScriptCommand(CommandBase commandBase){
        super("execute_script", "executeScriptDescription");
        this.commandBase = commandBase;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#executeScript()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.executeScript();
            String [] commandArr = str.trim().split(" ");
            if(str.length() == 0 || commandArr.length != 1 || arg != null){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }
            ResponseBuilder.appendln("executeScriptAnswer");
            return true;
        }
        catch (WrongAmountOfElements e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }
    }

}
