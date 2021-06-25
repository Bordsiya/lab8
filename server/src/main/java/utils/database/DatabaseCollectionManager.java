package utils.database;

import data.*;
import utils.ServerLauncher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Stack;
import java.util.logging.Level;

/**
 * Interaction with DB
 * @author NastyaBordun
 * @version 1.1
 */

public class DatabaseCollectionManager {

    //sql pre-statements
    private final String SELECT_ALL_SPACE_SHIPS = "SELECT * FROM " + DatabaseManager.SPACE_SHIPS_TABLE;
    private final String SELECT_COORDINATES_BY_SPACE_SHIP_ID = "SELECT * FROM " + DatabaseManager.COORDINATE_TABLE
            + " WHERE " + DatabaseManager.COORDINATE_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String SELECT_CHAPTER_BY_SPACE_SHIP_ID = "SELECT * FROM " + DatabaseManager.CHAPTER_TABLE
            + " WHERE " + DatabaseManager.CHAPTER_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String INSERT_SPACE_SHIP = "INSERT INTO " + DatabaseManager.SPACE_SHIPS_TABLE
            + "(" + DatabaseManager.SPACE_SHIPS_TABLE_CREATOR_ID_COLUMN + ", "
            + DatabaseManager.SPACE_SHIPS_TABLE_NAME_COLUMN + ", "
            + DatabaseManager.SPACE_SHIPS_TABLE_CREATION_DATE_COLUMN + ", "
            + DatabaseManager.SPACE_SHIPS_TABLE_HEALTH_COLUMN + ", "
            + DatabaseManager.SPACE_SHIPS_TABLE_ACHIEVEMENTS_COLUMN + ", "
            + DatabaseManager.SPACE_SHIPS_TABLE_WEAPON_TYPE_COLUMN + ", "
            + DatabaseManager.SPACE_SHIPS_TABLE_MELEE_WEAPON_COLUMN + ") VALUES(?, ?, ?, ?, ?, ?, ?)";
    private final String INSERT_CHAPTER = "INSERT INTO " + DatabaseManager.CHAPTER_TABLE
            + " (" + DatabaseManager.CHAPTER_TABLE_SHIP_ID_COLUMN + ", "
            + DatabaseManager.CHAPTER_TABLE_NAME_COLUMN + ", "
            + DatabaseManager.CHAPTER_TABLE_WORLD_COLUMN + ") VALUES (?, ?, ?)";
    private final String INSERT_COORDINATE = "INSERT INTO " + DatabaseManager.COORDINATE_TABLE
            + " (" + DatabaseManager.COORDINATE_TABLE_SHIP_ID_COLUMN + ", "
            + DatabaseManager.COORDINATE_TABLE_X_COLUMN + ", "
            + DatabaseManager.COORDINATE_TABLE_Y_COLUMN + ") VALUES(?, ?, ?)";
    private final String UPDATE_SPACE_SHIP_NAME_BY_ID = "UPDATE " + DatabaseManager.SPACE_SHIPS_TABLE
            + " SET " + DatabaseManager.SPACE_SHIPS_TABLE_NAME_COLUMN + " = ? WHERE "
            + DatabaseManager.SPACE_SHIPS_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String UPDATE_COORDINATE_X_BY_ID = "UPDATE " + DatabaseManager.COORDINATE_TABLE
            + " SET " + DatabaseManager.COORDINATE_TABLE_X_COLUMN + " = ? WHERE "
            + DatabaseManager.COORDINATE_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String UPDATE_COORDINATE_Y_BY_ID = "UPDATE " + DatabaseManager.COORDINATE_TABLE
            + " SET " + DatabaseManager.COORDINATE_TABLE_Y_COLUMN + " = ? WHERE "
            + DatabaseManager.COORDINATE_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String UPDATE_SPACE_SHIP_HEALTH_BY_ID = "UPDATE " + DatabaseManager.SPACE_SHIPS_TABLE
            + " SET " + DatabaseManager.SPACE_SHIPS_TABLE_HEALTH_COLUMN + " = ? WHERE "
            + DatabaseManager.SPACE_SHIPS_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String UPDATE_SPACE_SHIP_ACHIEVEMENTS_BY_ID = "UPDATE " + DatabaseManager.SPACE_SHIPS_TABLE
            + " SET " + DatabaseManager.SPACE_SHIPS_TABLE_ACHIEVEMENTS_COLUMN + " = ? WHERE "
            + DatabaseManager.SPACE_SHIPS_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String UPDATE_SPACE_SHIP_WEAPON_TYPE_BY_ID = "UPDATE " + DatabaseManager.SPACE_SHIPS_TABLE
            + " SET " + DatabaseManager.SPACE_SHIPS_TABLE_WEAPON_TYPE_COLUMN + " = ? WHERE "
            + DatabaseManager.SPACE_SHIPS_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String UPDATE_SPACE_SHIP_MELEE_WEAPON_BY_ID = "UPDATE " + DatabaseManager.SPACE_SHIPS_TABLE
            + " SET " + DatabaseManager.SPACE_SHIPS_TABLE_MELEE_WEAPON_COLUMN + " = ? WHERE "
            + DatabaseManager.SPACE_SHIPS_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String UPDATE_CHAPTER_NAME_BY_ID = "UPDATE " + DatabaseManager.CHAPTER_TABLE
            + " SET " + DatabaseManager.CHAPTER_TABLE_NAME_COLUMN + " = ? WHERE "
            + DatabaseManager.CHAPTER_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String UPDATE_CHAPTER_WORLD_BY_ID = "UPDATE " + DatabaseManager.CHAPTER_TABLE
            + " SET " + DatabaseManager.CHAPTER_TABLE_WORLD_COLUMN + " = ? WHERE "
            + DatabaseManager.CHAPTER_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String DELETE_SPACE_SHIP_BY_ID = "DELETE FROM " + DatabaseManager.SPACE_SHIPS_TABLE
            + " WHERE " + DatabaseManager.SPACE_SHIPS_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String DELETE_CHAPTER_BY_ID = "DELETE FROM " + DatabaseManager.CHAPTER_TABLE
            + " WHERE " + DatabaseManager.CHAPTER_TABLE_SHIP_ID_COLUMN + " = ?";
    private final String DELETE_COORDINATE_BY_ID = "DELETE FROM " + DatabaseManager.COORDINATE_TABLE
            + " WHERE " + DatabaseManager.COORDINATE_TABLE_SHIP_ID_COLUMN + " = ?";


    private DatabaseManager databaseManager;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseManager databaseManager, DatabaseUserManager databaseUserManager){
        this.databaseManager = databaseManager;
        this.databaseUserManager = databaseUserManager;
    }

    public Stack<SpaceMarine> getCollection() throws SQLException {
        Stack<SpaceMarine> collection = new Stack<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_ALL_SPACE_SHIPS, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                collection.push(createSpaceShip(resultSet));
            }
            return collection;
        }
        catch (SQLException e){
            throw new SQLException("Ошибка во время загрузки коллекции");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    private SpaceMarine createSpaceShip(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt(DatabaseManager.SPACE_SHIPS_TABLE_SHIP_ID_COLUMN);
        String name = resultSet.getString(DatabaseManager.SPACE_SHIPS_TABLE_NAME_COLUMN);
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseManager.SPACE_SHIPS_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        Float health = resultSet.getFloat(DatabaseManager.SPACE_SHIPS_TABLE_HEALTH_COLUMN);
        String achievements = resultSet.getString(DatabaseManager.SPACE_SHIPS_TABLE_ACHIEVEMENTS_COLUMN);
        Weapon weaponType = Weapon.valueOf(resultSet.getString(DatabaseManager.SPACE_SHIPS_TABLE_WEAPON_TYPE_COLUMN));
        MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(resultSet.getString(DatabaseManager.SPACE_SHIPS_TABLE_MELEE_WEAPON_COLUMN));
        Chapter chapter = getChapterBySpaceShipId(id);
        Coordinates coordinates = getCoordinatesBySpaceShipId(id);
        User creator = databaseUserManager.getUserById(resultSet.getInt(DatabaseManager.SPACE_SHIPS_TABLE_CREATOR_ID_COLUMN));
        return new SpaceMarine(id, name, creationDate, coordinates, health, achievements, weaponType, meleeWeapon, chapter, creator);
    }

    private Coordinates getCoordinatesBySpaceShipId(Integer spaceShipId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_COORDINATES_BY_SPACE_SHIP_ID, false);
            preparedStatement.setInt(1, spaceShipId);
            //System.out.println("getCoordinatesBySpaceShipId: " + preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                coordinates = new Coordinates(resultSet.getLong(DatabaseManager.COORDINATE_TABLE_X_COLUMN),
                        resultSet.getDouble(DatabaseManager.COORDINATE_TABLE_Y_COLUMN));
                return coordinates;
            }
            else throw new SQLException();
        }
        catch (SQLException e){
            //e.printStackTrace();
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка при выполнении запроса SELECT_COORDINATES_BY_SPACE_SHIP_ID");
            throw new SQLException("Ошибка при выполнении запроса SELECT_COORDINATES_BY_SPACE_SHIP_ID");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    private Chapter getChapterBySpaceShipId(Integer spaceShipId) throws SQLException {
        Chapter chapter;
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = databaseManager.getPreparedStatement(SELECT_CHAPTER_BY_SPACE_SHIP_ID, false);
            preparedStatement.setInt(1, spaceShipId);
            //System.out.println("getChapterBySpaceShipId" + preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                chapter = new Chapter(resultSet.getString(DatabaseManager.CHAPTER_TABLE_NAME_COLUMN),
                        resultSet.getString(DatabaseManager.CHAPTER_TABLE_WORLD_COLUMN));
                return chapter;
            }
            else throw new SQLException();
        }
        catch (SQLException e){
            //e.printStackTrace();
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка при выполнении запроса SELECT_CHAPTER_BY_SPACE_SHIP_ID");
            throw new SQLException("Ошибка при выполнении запроса SELECT_CHAPTER_BY_SPACE_SHIP_ID");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public SpaceMarine insertSpaceShip(SpaceMarineRaw spaceMarineRaw, User user) throws SQLException {
        PreparedStatement preparedStatementInsertSpaceShip = null;
        PreparedStatement preparedStatementInsertCoordinate = null;
        PreparedStatement preparedStatementInsertChapter = null;
        try{
            databaseManager.toNonAutoCommit();
            databaseManager.addSavePoint();
            preparedStatementInsertSpaceShip = databaseManager.getPreparedStatement(INSERT_SPACE_SHIP, true);
            preparedStatementInsertSpaceShip.setInt(1, databaseUserManager.getUserIdByUsernameAndPassword(user));
            preparedStatementInsertSpaceShip.setString(2, spaceMarineRaw.getName());
            preparedStatementInsertSpaceShip.setTimestamp(3, Timestamp.valueOf(spaceMarineRaw.getCreationDate()));
            preparedStatementInsertSpaceShip.setFloat(4, spaceMarineRaw.getHealth());
            preparedStatementInsertSpaceShip.setString(5, spaceMarineRaw.getAchievements());
            preparedStatementInsertSpaceShip.setString(6, spaceMarineRaw.getWeaponType().toString());
            preparedStatementInsertSpaceShip.setString(7, spaceMarineRaw.getMeleeWeapon().toString());
            //System.out.println("InsertSpaceShip: " + preparedStatementInsertSpaceShip);
            if(preparedStatementInsertSpaceShip.executeUpdate() == 0) throw new SQLException();
            Integer spaceShipId;
            ResultSet generatedSpaceShipKeys = preparedStatementInsertSpaceShip.getGeneratedKeys();
            if(generatedSpaceShipKeys.next()){
                spaceShipId = generatedSpaceShipKeys.getInt(1);
            }
            else throw new SQLException();
            ServerLauncher.logger.log(Level.INFO, "Выполнен запрос INSERT_SPACE_SHIP");

            preparedStatementInsertCoordinate = databaseManager.getPreparedStatement(INSERT_COORDINATE, false);
            preparedStatementInsertCoordinate.setInt(1, spaceShipId);
            preparedStatementInsertCoordinate.setLong(2, spaceMarineRaw.getCoordinates().getX());
            preparedStatementInsertCoordinate.setDouble(3, spaceMarineRaw.getCoordinates().getY());
            //System.out.println("InsertCoordinate: " + preparedStatementInsertCoordinate);
            if(preparedStatementInsertCoordinate.executeUpdate() == 0) throw new SQLException();
            ServerLauncher.logger.log(Level.INFO, "Выполнен запрос INSERT_COORDINATE");

            preparedStatementInsertChapter = databaseManager.getPreparedStatement(INSERT_CHAPTER, false);
            preparedStatementInsertChapter.setInt(1, spaceShipId);
            preparedStatementInsertChapter.setString(2, spaceMarineRaw.getChapter().getName());
            preparedStatementInsertChapter.setString(3, spaceMarineRaw.getChapter().getWorld());
            //System.out.println("InsertChapter: " + preparedStatementInsertChapter);
            if(preparedStatementInsertChapter.executeUpdate() == 0) throw new SQLException();
            ServerLauncher.logger.log(Level.INFO, "Выполнен запрос INSERT_CHAPTER");

            databaseManager.commit();

            return new SpaceMarine(spaceShipId, spaceMarineRaw.getName(),spaceMarineRaw.getCreationDate(),
                    spaceMarineRaw.getCoordinates(), spaceMarineRaw.getHealth(), spaceMarineRaw.getAchievements(),
                    spaceMarineRaw.getWeaponType(), spaceMarineRaw.getMeleeWeapon(), spaceMarineRaw.getChapter(),
                    user);
        }
        catch (SQLException e){
            //e.printStackTrace();
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка при добавления космического корабля в БД");
            databaseManager.rollback();
            throw new SQLException("Ошибка при добавления космического корабля в БД");
        }
        finally {
            databaseManager.closePreparedStatement(preparedStatementInsertSpaceShip);
            databaseManager.closePreparedStatement(preparedStatementInsertCoordinate);
            databaseManager.closePreparedStatement(preparedStatementInsertChapter);
            databaseManager.toAutoCommit();
        }

    }

    public void updateSpaceShipById(Integer spaceShipId, SpaceMarineRaw spaceMarineRaw) throws SQLException {
        PreparedStatement preparedStatementUpdateNameById = null;
        PreparedStatement preparedStatementUpdateCoordinateXById = null;
        PreparedStatement preparedStatementUpdateCoordinateYById = null;
        PreparedStatement preparedStatementUpdateHealthById = null;
        PreparedStatement preparedStatementUpdateAchievementsById = null;
        PreparedStatement preparedStatementUpdateWeaponTypeById = null;
        PreparedStatement preparedStatementUpdateMeleeWeaponById = null;
        PreparedStatement preparedStatementUpdateChapterNameById = null;
        PreparedStatement preparedStatementUpdateChapterWorldById = null;
        try{
            databaseManager.toNonAutoCommit();
            databaseManager.addSavePoint();

            preparedStatementUpdateNameById = databaseManager.getPreparedStatement(UPDATE_SPACE_SHIP_NAME_BY_ID, false);
            preparedStatementUpdateCoordinateXById = databaseManager.getPreparedStatement(UPDATE_COORDINATE_X_BY_ID, false);
            preparedStatementUpdateCoordinateYById = databaseManager.getPreparedStatement(UPDATE_COORDINATE_Y_BY_ID, false);
            //System.out.println("UpdateCoordinateYById: " + preparedStatementUpdateCoordinateYById);
            preparedStatementUpdateHealthById = databaseManager.getPreparedStatement(UPDATE_SPACE_SHIP_HEALTH_BY_ID, false);

            preparedStatementUpdateAchievementsById = databaseManager.getPreparedStatement(UPDATE_SPACE_SHIP_ACHIEVEMENTS_BY_ID, false);
            preparedStatementUpdateWeaponTypeById = databaseManager.getPreparedStatement(UPDATE_SPACE_SHIP_WEAPON_TYPE_BY_ID, false);
            preparedStatementUpdateMeleeWeaponById = databaseManager.getPreparedStatement(UPDATE_SPACE_SHIP_MELEE_WEAPON_BY_ID, false);
            preparedStatementUpdateChapterNameById = databaseManager.getPreparedStatement(UPDATE_CHAPTER_NAME_BY_ID, false);
            preparedStatementUpdateChapterWorldById = databaseManager.getPreparedStatement(UPDATE_CHAPTER_WORLD_BY_ID, false);

            if(spaceMarineRaw.getName() != null){
                preparedStatementUpdateNameById.setString(1, spaceMarineRaw.getName());
                preparedStatementUpdateNameById.setInt(2, spaceShipId);
                //System.out.println("UpdateNameById: " + preparedStatementUpdateNameById);
                if(preparedStatementUpdateNameById.executeUpdate() == 0) throw new SQLException();
                ServerLauncher.logger.log(Level.INFO, "Выполнен запрос UPDATE_SPACE_SHIP_NAME_BY_ID");
            }
            if(spaceMarineRaw.getCoordinates() != null){
                if(spaceMarineRaw.getCoordinates().getX() != 992){
                    preparedStatementUpdateCoordinateXById.setLong(1, spaceMarineRaw.getCoordinates().getX());
                    preparedStatementUpdateCoordinateXById.setInt(2, spaceShipId);
                    //System.out.println("UpdateCoordinateXById: " + preparedStatementUpdateCoordinateXById);
                    if(preparedStatementUpdateCoordinateXById.executeUpdate() == 0) throw new SQLException();
                    ServerLauncher.logger.log(Level.INFO, "Выполнен запрос UPDATE_COORDINATE_X_BY_ID");
                }
                if(spaceMarineRaw.getCoordinates().getY() != null){
                    preparedStatementUpdateCoordinateYById.setDouble(1, spaceMarineRaw.getCoordinates().getY());
                    preparedStatementUpdateCoordinateYById.setInt(2, spaceShipId);
                    //System.out.println("UpdateCoordinateYById: " + preparedStatementUpdateCoordinateYById);
                    if(preparedStatementUpdateCoordinateYById.executeUpdate() == 0) throw new SQLException();
                    ServerLauncher.logger.log(Level.INFO, "Выполнен запрос UPDATE_COORDINATE_Y_BY_ID");
                }
            }
            if(spaceMarineRaw.getHealth() != null){
                preparedStatementUpdateHealthById.setFloat(1, spaceMarineRaw.getHealth());
                preparedStatementUpdateHealthById.setInt(2,spaceShipId);
                //System.out.println("UpdateHealthById: " + preparedStatementUpdateHealthById);
                if(preparedStatementUpdateHealthById.executeUpdate() == 0) throw new SQLException();
                ServerLauncher.logger.log(Level.INFO, "Выполнен запрос UPDATE_SPACE_SHIP_HEALTH_BY_ID");
            }
            if(spaceMarineRaw.getAchievements() != null){
                preparedStatementUpdateAchievementsById.setString(1, spaceMarineRaw.getAchievements());
                preparedStatementUpdateAchievementsById.setInt(2, spaceShipId);
                //System.out.println("UpdateAchievementsById: " + preparedStatementUpdateAchievementsById);
                if(preparedStatementUpdateAchievementsById.executeUpdate() == 0) throw new SQLException();
                ServerLauncher.logger.log(Level.INFO, "Выполнен запрос UPDATE_SPACE_SHIP_ACHIEVEMENTS_BY_ID");
            }
            if(spaceMarineRaw.getWeaponType() != null){
                preparedStatementUpdateWeaponTypeById.setString(1, spaceMarineRaw.getWeaponType().toString());
                preparedStatementUpdateWeaponTypeById.setInt(2, spaceShipId);
                //System.out.println("UpdateWeaponTypeById: " + preparedStatementUpdateWeaponTypeById);
                if(preparedStatementUpdateWeaponTypeById.executeUpdate() == 0) throw new SQLException();
                ServerLauncher.logger.log(Level.INFO, "Выполнен запрос UPDATE_SPACE_SHIP_WEAPON_TYPE_BY_ID");
            }
            if(spaceMarineRaw.getMeleeWeapon() != null){
                preparedStatementUpdateMeleeWeaponById.setString(1, spaceMarineRaw.getMeleeWeapon().toString());
                preparedStatementUpdateMeleeWeaponById.setInt(2, spaceShipId);
                //System.out.println("UpdateMeleeWeaponById: " + preparedStatementUpdateMeleeWeaponById);
                if(preparedStatementUpdateMeleeWeaponById.executeUpdate() == 0) throw new SQLException();
                ServerLauncher.logger.log(Level.INFO, "Выполнен запрос UPDATE_SPACE_SHIP_MELEE_WEAPON_BY_ID");
            }
            if(spaceMarineRaw.getChapter() != null){
                if(spaceMarineRaw.getChapter().getName() != null){
                    preparedStatementUpdateChapterNameById.setString(1, spaceMarineRaw.getChapter().getName());
                    preparedStatementUpdateChapterNameById.setInt(2, spaceShipId);
                    //System.out.println("UpdateChapterNameById: " + preparedStatementUpdateChapterNameById);
                    if(preparedStatementUpdateChapterNameById.executeUpdate() == 0) throw new SQLException();
                    ServerLauncher.logger.log(Level.INFO, "Выполнен запрос UPDATE_CHAPTER_NAME_BY_ID");
                }
                if(spaceMarineRaw.getChapter().getWorld() != null){
                    preparedStatementUpdateChapterWorldById.setString(1, spaceMarineRaw.getChapter().getWorld());
                    preparedStatementUpdateChapterWorldById.setInt(2, spaceShipId);
                    //System.out.println("UpdateChapterWorldById: " + preparedStatementUpdateChapterWorldById);
                    if(preparedStatementUpdateChapterWorldById.executeUpdate() == 0) throw new SQLException();
                    ServerLauncher.logger.log(Level.INFO, "Выполнен запрос UPDATE_CHAPTER_WORLD_BY_ID");
                }
            }

            databaseManager.commit();
        }
        catch (SQLException e){
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка при обновлении объекта");
            databaseManager.rollback();
            throw new SQLException("Ошибка при добавления космического корабля в БД");
        }
        finally{
            databaseManager.closePreparedStatement(preparedStatementUpdateNameById);
            databaseManager.closePreparedStatement(preparedStatementUpdateCoordinateXById);
            databaseManager.closePreparedStatement(preparedStatementUpdateCoordinateYById);
            databaseManager.closePreparedStatement(preparedStatementUpdateHealthById);
            databaseManager.closePreparedStatement(preparedStatementUpdateAchievementsById);
            databaseManager.closePreparedStatement(preparedStatementUpdateWeaponTypeById);
            databaseManager.closePreparedStatement(preparedStatementUpdateMeleeWeaponById);
            databaseManager.closePreparedStatement(preparedStatementUpdateChapterNameById);
            databaseManager.closePreparedStatement(preparedStatementUpdateChapterWorldById);
            databaseManager.toAutoCommit();
        }
    }

    public void deleteSpaceShipById(Integer spaceShipId) throws SQLException {
        PreparedStatement preparedStatementDeleteSpaceShipById = null;
        PreparedStatement preparedStatementDeleteChapterById = null;
        PreparedStatement preparedStatementDeleteCoordinateById = null;
        try{
            databaseManager.toNonAutoCommit();
            databaseManager.addSavePoint();

            preparedStatementDeleteSpaceShipById = databaseManager.getPreparedStatement(DELETE_SPACE_SHIP_BY_ID, false);
            preparedStatementDeleteChapterById = databaseManager.getPreparedStatement(DELETE_CHAPTER_BY_ID, false);
            preparedStatementDeleteCoordinateById = databaseManager.getPreparedStatement(DELETE_COORDINATE_BY_ID, false);

            preparedStatementDeleteChapterById.setInt(1, spaceShipId);
            //System.out.println("DeleteChapterById: " + preparedStatementDeleteChapterById);
            if(preparedStatementDeleteChapterById.executeUpdate() == 0) throw new SQLException();
            ServerLauncher.logger.log(Level.INFO, "Выполнен запрос DELETE_CHAPTER_BY_ID");

            preparedStatementDeleteCoordinateById.setInt(1, spaceShipId);
            //System.out.println("DeleteCoordinateById: " + preparedStatementDeleteCoordinateById);
            if(preparedStatementDeleteCoordinateById.executeUpdate() == 0) throw new SQLException();
            ServerLauncher.logger.log(Level.INFO, "Выполнен запрос DELETE_COORDINATE_BY_ID");

            preparedStatementDeleteSpaceShipById.setInt(1, spaceShipId);
            //System.out.println("DeleteSpaceShipById: " + preparedStatementDeleteSpaceShipById);
            if(preparedStatementDeleteSpaceShipById.executeUpdate() == 0) throw new SQLException();
            ServerLauncher.logger.log(Level.INFO, "Выполнен запрос DELETE_SPACE_SHIP_BY_ID");

            databaseManager.commit();

        }
        catch (SQLException e){
            //e.printStackTrace();
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка при удалении объекта");
            databaseManager.rollback();
            throw new SQLException("Ошибка при удалении космического корабля из БД");
        }
        finally{
            databaseManager.closePreparedStatement(preparedStatementDeleteSpaceShipById);
            databaseManager.closePreparedStatement(preparedStatementDeleteChapterById);
            databaseManager.closePreparedStatement(preparedStatementDeleteCoordinateById);
            databaseManager.toAutoCommit();
        }
    }

    public void clearCollection() throws SQLException {
        Stack<SpaceMarine> spaceMarines = getCollection();
        for(SpaceMarine sp: spaceMarines){
            deleteSpaceShipById(sp.getId());
        }
    }

}
