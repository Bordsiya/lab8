package commands;

import data.User;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;

/**
 * Command for program exiting
 * @author NastyaBordun
 * @version 1.1
 */

public class ExitCommand extends AbstractCommand{
    /**
     * Base for all commands {@link CommandBase}
     */
    private CommandBase commandBase;
    private CollectionManager collectionManager;

    /**
     * Constructor for the command
     * @param commandBase base for commands
     */
    public ExitCommand(CommandBase commandBase, CollectionManager collectionManager){
        super("exit", "exitScriptDescription");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#exit()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.exit();
            if(str.length() != 0 || arg != null){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }
            ResponseBuilder.appendln("exitAnswer");
            return true;
        }
        catch (WrongAmountOfElements e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }
    }

}
