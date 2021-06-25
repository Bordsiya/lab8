package utils.database;

import data.User;
import utils.ServerLauncher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Database manager for user
 * @author NastyaBordun
 * @version 1.1
 */

public class DatabaseUserManager {

    //USERS_TABLE sql pre-statements
    private final String INSERT_USER = "INSERT INTO " + DatabaseManager.USERS_TABLE
            + " (" + DatabaseManager.USERS_TABLE_USERNAME_COLUMN + ", "
            + DatabaseManager.USERS_TABLE_PASSWORD_COLUMN + ")"
            + "VALUES (?, ?)";

    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = "SELECT * FROM "
            + DatabaseManager.USERS_TABLE + " WHERE " + DatabaseManager.USERS_TABLE_USERNAME_COLUMN
            + " = ? AND " + DatabaseManager.USERS_TABLE_PASSWORD_COLUMN + " = ?";

    private final String SELECT_USER_BY_ID = "SELECT * FROM "
            + DatabaseManager.USERS_TABLE + " WHERE " + DatabaseManager.USERS_TABLE_ID_COLUMN
            + " = ?";
    private final String SELECT_USER_ID_BY_USERNAME_AND_PASSWORD = "SELECT " + DatabaseManager.USERS_TABLE_ID_COLUMN
            + " FROM " + DatabaseManager.USERS_TABLE + " WHERE " + DatabaseManager.USERS_TABLE_USERNAME_COLUMN
            + " = ? AND " + DatabaseManager.USERS_TABLE_PASSWORD_COLUMN + " = ?";

    private DatabaseManager databaseManager;

    public DatabaseUserManager(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }

    public boolean checkUserByUsernameAndPassword(User user) throws SQLException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = databaseManager.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            //System.out.println("Запрос: " + preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            ServerLauncher.logger.log(Level.INFO, "Выполнен запрос SELECT_USER_BY_USERNAME_AND_PASSWORD");
            return resultSet.next();
        }
        catch (SQLException e){
            //e.printStackTrace();
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка во время выполнения запроса SELECT_USER_BY_USERNAME_AND_PASSWORD");
            throw new SQLException("Ошибка во время выполнения запроса SELECT_USER_BY_USERNAME_AND_PASSWORD");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public boolean insertUserToDatabase(User user) throws SQLException {
        PreparedStatement preparedStatement = null;
        try{
            if (checkUserByUsernameAndPassword(user)) return false;
            preparedStatement = databaseManager.getPreparedStatement(INSERT_USER, false);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            //System.out.println("insertUserToDatabase: " + preparedStatement);
            if(preparedStatement.executeUpdate() == 0) throw new SQLException();
            ServerLauncher.logger.log(Level.INFO, "Выполнен запрос INSERT_USER");
            return true;
        }
        catch (SQLException e){
            //e.printStackTrace();
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка во время выполнения запроса INSERT_USER");
            throw new SQLException("Ошибка во время выполнения запроса INSERT_USER");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }

    }

    public User getUserById(Integer id) throws SQLException {
        User user;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_USER_BY_ID, false);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user = new User(resultSet.getString(DatabaseManager.USERS_TABLE_USERNAME_COLUMN),
                        resultSet.getString(DatabaseManager.USERS_TABLE_PASSWORD_COLUMN));
            }
            else throw new SQLException();
            return user;
        }
        catch (SQLException e){
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка во время выполнения запроса SELECT_USER_BY_ID");
            throw new SQLException("Ошибка во время выполнения запроса SELECT_USER_BY_ID");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }

    }

    public Integer getUserIdByUsernameAndPassword(User user) throws SQLException {
        Integer id;
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = databaseManager.getPreparedStatement(SELECT_USER_ID_BY_USERNAME_AND_PASSWORD, false);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                id = resultSet.getInt(DatabaseManager.USERS_TABLE_ID_COLUMN);
            }
            else throw new SQLException();
            return id;
        }
        catch (SQLException e){
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка во время выполнения запроса SELECT_USER_ID_BY_USERNAME_AND_PASSWORD");
            throw new SQLException("Ошибка во время выполнения запроса SELECT_USER_ID_BY_USERNAME_AND_PASSWORD");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

}
