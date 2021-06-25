package commands;

import data.*;
import exceptions.PermissionDeniedException;
import exceptions.SpaceMarineNotFoundException;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;
import utils.database.DatabaseCollectionManager;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * The Update Value Command of a Collection Element by ID
 * @author NastyaBordun
 * @version 1.1
 */

public class UpdateCommand extends AbstractCommand{
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
    public UpdateCommand(CommandBase commandBase, CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("update", "updateDescription");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#update()
     * @see CollectionManager#searchById(Integer)
     * @see SpaceMarine#setName(String)
     * @see SpaceMarine#setCoordinateX(long)
     * @see SpaceMarine#setCoordinateY(Double)
     * @see SpaceMarine#setHealth(Float)
     * @see SpaceMarine#setAchievements(String)
     * @see SpaceMarine#setWeaponType(Weapon)
     * @see SpaceMarine#setMeleeWeapon(MeleeWeapon)
     * @see SpaceMarine#setChapterName(String)
     * @see SpaceMarine#setChapterWorld(String)
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.update();
            String [] commandArr = str.split(" ");
            if(str.length() == 0 || commandArr.length != 1 || arg == null){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }
            Integer id = Integer.parseInt(commandArr[0]);
            SpaceMarine oldSpaceMarine = collectionManager.searchById(id);
            if(oldSpaceMarine == null) throw new SpaceMarineNotFoundException("spaceMarineNotFoundException");
            if(!oldSpaceMarine.getCreator().equals(user)) throw new PermissionDeniedException("permissionDeniedException");

            SpaceMarineRaw newMarineRaw = (SpaceMarineRaw) arg;

            databaseCollectionManager.updateSpaceShipById(id, newMarineRaw);

            String name = newMarineRaw.getName() == null ? oldSpaceMarine.getName() : newMarineRaw.getName();
            Coordinates coordinates = oldSpaceMarine.getCoordinates();
            if(newMarineRaw.getCoordinates().getX() != 992){
                coordinates.setX(newMarineRaw.getCoordinates().getX());
            }
            if(newMarineRaw.getCoordinates().getY() != null){
                coordinates.setY(newMarineRaw.getCoordinates().getY());
            }
            LocalDateTime creationDate = oldSpaceMarine.getCreationDate();
            Float health = newMarineRaw.getHealth() == null ? oldSpaceMarine.getHealth() : newMarineRaw.getHealth();
            String achievements = newMarineRaw.getAchievements() == null ? oldSpaceMarine.getAchievements() : newMarineRaw.getAchievements();
            Weapon weaponType = newMarineRaw.getWeaponType() == null ? oldSpaceMarine.getWeaponType() : newMarineRaw.getWeaponType();
            MeleeWeapon meleeWeapon = newMarineRaw.getMeleeWeapon() == null ? oldSpaceMarine.getMeleeWeapon() : newMarineRaw.getMeleeWeapon();
            Chapter chapter = oldSpaceMarine.getChapter();
            if(newMarineRaw.getChapter().getName() != null) chapter.setName(newMarineRaw.getChapter().getName());
            if(newMarineRaw.getChapter().getWorld() != null) chapter.setWorld(newMarineRaw.getChapter().getWorld());

            oldSpaceMarine.setName(name);
            oldSpaceMarine.setCoordinates(coordinates);
            oldSpaceMarine.setCreationDate(creationDate);
            oldSpaceMarine.setHealth(health);
            oldSpaceMarine.setAchievements(achievements);
            oldSpaceMarine.setWeaponType(weaponType);
            oldSpaceMarine.setMeleeWeapon(meleeWeapon);
            oldSpaceMarine.setChapter(chapter);

            ResponseBuilder.appendln("updateAnswer");
            return true;
        }
        catch (WrongAmountOfElements | SpaceMarineNotFoundException | PermissionDeniedException e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }
        catch (NumberFormatException e){
            ResponseBuilder.appendError("incorrectIdException");
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
