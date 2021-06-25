package utils;

import commands.*;
import exceptions.WrongAmountOfElements;
import utils.database.DatabaseCollectionManager;
import utils.database.DatabaseManager;
import utils.database.DatabaseUserManager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for server launching
 * @author NastyaBordun
 * @version 1.1
 */
public class ServerLauncher {
    private static final String databaseLogin = "postgres";
    private static String databaseHost;
    private static String databasePassword;
    private static String databaseAddress;
    public static final int PORT = 1246;
    public static Logger logger = Logger.getLogger("ServerLogger");

    public void launchServer(String [] args){
            if(!uploadDatabaseVariables(args)) return;
            DatabaseManager databaseManager = new DatabaseManager(databaseAddress, databaseLogin, databasePassword);
            DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseManager);
            DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseManager, databaseUserManager);

            CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);

            CommandBase commandBase = new CommandBase();
            CommandManager commandManager = new CommandManager(
                    new InfoCommand(commandBase, collectionManager),
                    new ShowCommand(commandBase, collectionManager),
                    new AddCommand(commandBase, collectionManager, databaseCollectionManager),
                    new UpdateCommand(commandBase, collectionManager, databaseCollectionManager),
                    new RemoveCommand(commandBase, collectionManager, databaseCollectionManager),
                    new ClearCommand(commandBase, collectionManager, databaseCollectionManager),
                    new ExecuteScriptCommand(commandBase),
                    new ExitCommand(commandBase, collectionManager),
                    new RemoveGreaterCommand(commandBase, collectionManager, databaseCollectionManager),
                    new RemoveLowerCommand(commandBase, collectionManager, databaseCollectionManager),
                    new ReorderCommand(commandBase, collectionManager),
                    new FilterAchievementsCommand(commandBase, collectionManager),
                    new AscendingWeaponTypeCommand(commandBase, collectionManager),
                    new DescendingAchievementsCommand(commandBase, collectionManager),
                    new LoginCommand(commandBase, databaseUserManager),
                    new RegisterCommand(commandBase, databaseUserManager));
            commandManager.addCommand(new HelpCommand(commandBase, commandManager));

            Receiver receiver = new Receiver();
            Server server = new Server(commandManager, receiver, collectionManager);
            logger.log(Level.INFO, "Сервер запущен");
            server.run();
            databaseManager.closeConnection();
    }

    private boolean uploadDatabaseVariables(String [] args){
        try{
            if(args.length != 2) throw new WrongAmountOfElements("Неверное количество аргументов");
            databaseHost = args[0];
            databasePassword = args[1];
            databaseAddress = "jdbc:postgresql://" + databaseHost + ":5432/studs1";
            //System.out.println("Хост: " + args[0] + ", пароль: " + args[1]);
            return true;
        }
        catch (WrongAmountOfElements e){
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
    }
}
