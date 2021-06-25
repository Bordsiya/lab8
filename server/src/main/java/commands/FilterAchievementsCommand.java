package commands;

import data.SpaceMarine;
import data.User;
import exceptions.EmptyCollectionException;
import exceptions.NoMatchException;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;

import java.util.ArrayList;

/**
 * Printing command for collection elements with type {@link SpaceMarine}, whose achievements field value starts with the specified substring
 * @author NastyaBordun
 * @version 1.1
 */

public class FilterAchievementsCommand extends AbstractCommand{
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
    public FilterAchievementsCommand(CommandBase commandBase, CollectionManager collectionManager){
        super("filter_starts_with_achievements", "filterAchievementsDescription");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#filterAchievements()
     * @see CollectionManager#startsWithAchievements(String)
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            //commandBase.filterAchievements();
            String [] commandArr = str.trim().split(" ");
            if(str.length() == 0 || commandArr.length != 1 || arg != null){
                throw new WrongAmountOfElements("wrongAmountOfElements");
            }

            if(collectionManager.collectionSize() == 0){
                throw new EmptyCollectionException("emptyCollectionException");
            }
            ArrayList<SpaceMarine> spaceMarines = collectionManager.startsWithAchievements(str);
            if(spaceMarines.size() == 0){
                throw new NoMatchException("noMatchException");
            }

            ArrayList<String> argsList = new ArrayList<>();
            for(SpaceMarine sm : spaceMarines){
                argsList.add(sm.getId().toString());
            }
            String[] args = new String[argsList.size()];
            args = argsList.toArray(args);
            ResponseBuilder.appendArgs(args);
            ResponseBuilder.appendln("filterAchAnswer");
            return true;
        }
        catch (EmptyCollectionException | NoMatchException | WrongAmountOfElements e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }

    }

}
