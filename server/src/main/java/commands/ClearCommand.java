package commands;

import data.SpaceMarine;
import data.User;
import exceptions.EmptyCollectionException;
import exceptions.PermissionDeniedException;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;
import utils.database.DatabaseCollectionManager;

import java.sql.SQLException;

/**
 * Command for collection clearing
 * @author NastyaBordun
 * @version 1.1
 */

public class ClearCommand extends AbstractCommand{
    /**
     * Base for all commands {@link CommandBase}
     */
    private CommandBase commandBase;
    /**
     * Manager for collection {@link CollectionManager}
     */
    private CollectionManager collectionManager;

    private DatabaseCollectionManager databaseCollectionManager;
    /**
     * Constructor for the command
     * @param commandBase base for commands
     * @param collectionManager collection manager
     */
    public ClearCommand(CommandBase commandBase, CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("clear", "clearDescription");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#clear()
     * @see CollectionManager#collectionSize()
     * @see CollectionManager#clearCollection()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.clear();
            if(str.length() != 0 || arg != null){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }

            for(SpaceMarine sp: collectionManager.getCollection()){
                if(!sp.getCreator().equals(user)) throw new PermissionDeniedException("permissionDeniedException");
            }
            if(collectionManager.collectionSize() == 0){
                throw new EmptyCollectionException("emptyCollectionException");
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clearCollection();

            ResponseBuilder.appendln("clearAnswer");
            return true;
        }
        catch (EmptyCollectionException | WrongAmountOfElements | PermissionDeniedException e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }
        catch (SQLException e){
            ResponseBuilder.appendError("errorWithDbException");
            return false;
        }
    }

}
