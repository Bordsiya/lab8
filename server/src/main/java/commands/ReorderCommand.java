package commands;

import data.User;
import exceptions.EmptyCollectionException;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;

/**
 * Sorting command of the collection in reverse order
 * @author NastyaBordun
 * @version 1.1
 */

public class ReorderCommand extends AbstractCommand{
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
    public ReorderCommand(CommandBase commandBase, CollectionManager collectionManager){
        super("reorder", "reorderDescription");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#reorder()
     * @see CollectionManager#collectionSize()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.reorder();
            if(str.length() != 0 || arg !=null){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }

            String info = collectionManager.getReorderedCollection();
            ResponseBuilder.appendln(info);
            return true;
        }
        catch (WrongAmountOfElements | EmptyCollectionException e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }

    }

}
