package utils.database;

import utils.ResponseBuilder;
import utils.ServerLauncher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Manager for DB
 * @author NastyaBordun
 * @version 1.1
 */

public class DatabaseManager {

    //Table names
    public static final String USERS_TABLE = "users";
    public static final String CHAPTER_TABLE = "chapter";
    public static final String COORDINATE_TABLE = "coordinate";
    public static final String SPACE_SHIPS_TABLE = "space_ships";

    //USERS_TABLE columns
    public static final String USERS_TABLE_ID_COLUMN = "id";
    public static final String USERS_TABLE_USERNAME_COLUMN = "username";
    public static final String USERS_TABLE_PASSWORD_COLUMN = "password";

    //CHAPTER_TABLE columns
    public static final String CHAPTER_TABLE_SHIP_ID_COLUMN = "ship_id";
    public static final String CHAPTER_TABLE_NAME_COLUMN = "name";
    public static final String CHAPTER_TABLE_WORLD_COLUMN = "world";

    //COORDINATE_TABLE columns
    public static final String COORDINATE_TABLE_SHIP_ID_COLUMN = "ship_id";
    public static final String COORDINATE_TABLE_X_COLUMN = "x";
    public static final String COORDINATE_TABLE_Y_COLUMN = "y";

    //SPACE_SHIPS_TABLE columns
    public static final String SPACE_SHIPS_TABLE_SHIP_ID_COLUMN = "ship_id";
    public static final String SPACE_SHIPS_TABLE_CREATOR_ID_COLUMN = "creator_id";
    public static final String SPACE_SHIPS_TABLE_NAME_COLUMN = "name";
    public static final String SPACE_SHIPS_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String SPACE_SHIPS_TABLE_HEALTH_COLUMN = "health";
    public static final String SPACE_SHIPS_TABLE_ACHIEVEMENTS_COLUMN = "achievements";
    public static final String SPACE_SHIPS_TABLE_WEAPON_TYPE_COLUMN = "weapon_type";
    public static final String SPACE_SHIPS_TABLE_MELEE_WEAPON_COLUMN = "melee_weapon";

    private final String address;
    private final String user;
    private final String password;
    private final String DRIVER = "org.postgresql.Driver";
    private Connection connection;

    public DatabaseManager(String address, String user, String password){
        this.address = address;
        this.user = user;
        this.password = password;
        connectToDatabase();
    }

    private void connectToDatabase(){
        ResponseBuilder.appendln("Попытка подключения к БД");
        ServerLauncher.logger.log(Level.INFO, "Попытка подключения к БД");
        try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(address, user, password);
            ResponseBuilder.appendln("Подключились к БД");
            ServerLauncher.logger.log(Level.INFO, "Подключились к БД");
        } catch (ClassNotFoundException e) {
            ResponseBuilder.appendError("Драйвер для данной БД не найден");
            ServerLauncher.logger.log(Level.SEVERE, "Драйвер для данной БД не найден");
        }
        catch (SQLException e){
            ResponseBuilder.appendError("Ошибка при подключении к БД");
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка при подключении к БД");
        }

    }

    public void closeConnection(){
        if(connection == null) return;
        try {
            connection.close();
        }
        catch (SQLException e){
            ResponseBuilder.appendError("Ошибка при закрытии БД");
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка при закрытии БД");
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generationMode) throws SQLException {
        if(connection == null) throw new SQLException("Ошибка при формировании запроса");
        int key = generationMode ? PreparedStatement.RETURN_GENERATED_KEYS : PreparedStatement.NO_GENERATED_KEYS;
        return connection.prepareStatement(sqlStatement, key);
    }

    public void closePreparedStatement(PreparedStatement preparedStatement){
        if(preparedStatement == null) return;
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка во время закрытия запроса " + preparedStatement);
        }
    }

    public void toNonAutoCommit(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время изменения режима сохранения");
            connection.setAutoCommit(false);
        }
        catch (SQLException e){
            ServerLauncher.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public void toAutoCommit(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время изменения режима сохранения");
            connection.setAutoCommit(true);
        }
        catch (SQLException e){
            ServerLauncher.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public void commit(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время сохранения состояния БД");
            connection.commit();
        }
        catch (SQLException e){
            ServerLauncher.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public void rollback(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время отката состояния БД");
            connection.rollback();
        }
        catch (SQLException e){
            ServerLauncher.logger.log(Level.SEVERE, e.getMessage());
        }

    }

    public void addSavePoint(){
        try {
            if (connection == null) throw new SQLException("Ошибка во время добавления точки сохранения");
            connection.setSavepoint();
        }
        catch (SQLException e){
            ServerLauncher.logger.log(Level.SEVERE, e.getMessage());
        }

    }


}
