package commands;

import data.User;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;

import java.time.format.DateTimeFormatter;

/**
 * Printing command for collection information
 * @author NastyaBordun
 * @version 1.1
 */

public class InfoCommand extends AbstractCommand{
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
    public InfoCommand(CommandBase commandBase, CollectionManager collectionManager){
        super("info", "infoDescription");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#info()
     * @see CollectionManager#collectionSize()
     * @see CollectionManager#getLastInit()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.info();
            if(str.length() != 0 || arg != null){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ResponseBuilder.appendln("infoAnswer");
            String[] args = new String[4];
            args[0] = "collectionSize";
            args[1] = String.valueOf(collectionManager.collectionSize());
            args[2] = "initDate";
            args[3] = collectionManager.getLastInit().format(formatter);
            ResponseBuilder.appendArgs(args);
            return true;
        }
        catch (WrongAmountOfElements e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }

    }

}
