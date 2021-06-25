package commands;

import data.SpaceMarine;
import data.SpaceMarineRaw;
import data.User;
import exceptions.PermissionDeniedException;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;
import utils.database.DatabaseCollectionManager;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * The Removal Command for collection elements with type {@link SpaceMarine}, lower than assigned element
 * @author NastyaBordun
 * @version 1.1
 */

public class RemoveLowerCommand extends AbstractCommand{
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
    public RemoveLowerCommand(CommandBase commandBase, CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("remove_lower", "removeLowerDescription");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#removeLower()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.removeLower();
            if(str.length() != 0 || arg == null){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }

            SpaceMarineRaw spaceMarineRaw = (SpaceMarineRaw)arg;
            SpaceMarine spaceMarine = new SpaceMarine(
                    0,
                    spaceMarineRaw.getName(),
                    LocalDateTime.now(),
                    spaceMarineRaw.getCoordinates(),
                    spaceMarineRaw.getHealth(),
                    spaceMarineRaw.getAchievements(),
                    spaceMarineRaw.getWeaponType(),
                    spaceMarineRaw.getMeleeWeapon(),
                    spaceMarineRaw.getChapter(),
                    user);
            for(SpaceMarine sp: collectionManager.getLower(spaceMarine)){
                if(!sp.getCreator().equals(user)) throw new PermissionDeniedException("permissionDeniedException");
            }
            for(SpaceMarine sp: collectionManager.getLower(spaceMarine)){
                databaseCollectionManager.deleteSpaceShipById(sp.getId());
                collectionManager.removeElementById(sp.getId());
            }

            ResponseBuilder.appendln("removeLowerAnswer");
            return true;
        }
        catch (WrongAmountOfElements | PermissionDeniedException e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }
        catch (ClassCastException e){
            ResponseBuilder.appendError("classCastException");
            return false;
        } catch (SQLException e) {
            ResponseBuilder.appendError("errorWithDbException");
            return false;
        }
    }

}
