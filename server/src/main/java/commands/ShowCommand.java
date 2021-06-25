package commands;

import data.User;
import exceptions.EmptyCollectionException;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;

/**
 * Command for printing all of collection elements
 * @author NastyaBordun
 * @version 1.1
 */

public class ShowCommand extends AbstractCommand{
    /**
     * Base for all commands {@link CommandBase}
     */
    private CommandBase commandBase;
    /**
     * Manager for collection {@link CollectionManager}
     */
    private CollectionManager collectionManager;
    /**
     * Constructor for the command
     * @param commandBase base for commands
     * @param collectionManager collection manager
     */
    public ShowCommand(CommandBase commandBase, CollectionManager collectionManager){
        super("show", "showDescription");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#show()
     * @see CollectionManager#getStringCollection()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.show();
            if(str.length() != 0 || arg!=null){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }
            ResponseBuilder.appendln("showAnswer");
            return true;
        }
        catch (WrongAmountOfElements e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }

    }

}
