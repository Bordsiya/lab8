package commands;

import answers.Response;
import data.User;
import exceptions.EmptyCollectionException;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;

/**
 * Printing command for field weaponType of all collection elements, with types {@link data.SpaceMarine}, in ascending order
 * @author NastyaBordun
 * @version 1.1
 */

public class AscendingWeaponTypeCommand extends AbstractCommand{
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
    public AscendingWeaponTypeCommand(CommandBase commandBase, CollectionManager collectionManager){
        super("print_field_ascending_weapon_type", "ascWeaponTypeDescription");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#ascendingWeaponType()
     * @see CollectionManager#collectionSize()
     * @see CollectionManager#ascendWeaponType()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.ascendingWeaponType();
            if(str.length() != 0 || arg != null ){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }

            if(collectionManager.collectionSize() == 0){
                throw new EmptyCollectionException("emptyCollectionException");
            }

            String[] info = collectionManager.ascendWeaponType();
            ResponseBuilder.appendln("ascWeaponTypeAnswer");
            ResponseBuilder.appendArgs(info);
            return true;
        }
        catch (EmptyCollectionException | WrongAmountOfElements e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }

    }

}
