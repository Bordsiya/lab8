package controllers;

import data.*;
import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.Business;
import utils.ObservableResourceFactory;
import utils.UIOutputer;
import java.io.IOException;
import java.time.LocalDateTime;

public class EditSpaceShipController {

    private Business business;

    public void setBusiness(Business business) {
        this.business = business;
    }

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label creationDateLabel;

    @FXML
    private Label healthLabel;

    @FXML
    private Label achievementsLabel;

    @FXML
    private Label weaponTypeLabel;

    @FXML
    private Label meleeWeaponLabel;

    @FXML
    private Label xLabel;

    @FXML
    private Label yLabel;

    @FXML
    private Label chapterNameLabel;

    @FXML
    private Label chapterWorldLabel;

    @FXML
    private Label creatorLabel;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField health;

    @FXML
    private TextField achievements;

    @FXML
    private ChoiceBox<Weapon> weaponType;

    @FXML
    private ChoiceBox<MeleeWeapon> meleeWeapon;

    @FXML
    private TextField x;

    @FXML
    private TextField y;

    @FXML
    private TextField chapterName;

    @FXML
    private TextField chapterWorld;

    @FXML
    private Label editTitle;

    @FXML
    private Button btn_send;

    private ObservableResourceFactory resourceFactory;
    private String lastNeedCommand;
    private Stage editSpaceShipStage;
    private SpaceMarineRaw resultMarine;
    private Integer graphId;

    public void setEditSpaceShipStage(Stage editSpaceShipStage){
        this.editSpaceShipStage = editSpaceShipStage;
    }

    public void setLastNeedCommand(String lastNeedCommand) {
        this.lastNeedCommand = lastNeedCommand;
    }


    public void setGraphId(Integer id){
        this.graphId = id;
    }

    public SpaceMarineRaw getResultMarine(){
        return resultMarine;
    }

    public void clearFields(){
        resultMarine = null;
        weaponType.getItems().clear();
        meleeWeapon.getItems().clear();
        id.setText("");
        name.setText("");
        health.setText("");
        achievements.setText("");
        x.setText("");
        y.setText("");
        chapterName.setText("");
        chapterWorld.setText("");
    }

    public void toSendPress(ActionEvent actionEvent) throws IOException {
        try{
            if(lastNeedCommand.equals("update")){
                updateSpaceShip();
                return;
            }
            String shipName = business.parseName(name.getText());
            Float shipHealth = business.parseHealth(health.getText());
            String shipAchievements = business.parseAchievements(achievements.getText());
            Weapon shipWeaponType = weaponType.getValue();
            MeleeWeapon shipMeleeWeapon = meleeWeapon.getValue();
            long shipX = business.parseX(x.getText());
            Double shipY = business.parseY(y.getText());
            String shipChapterName = business.parseChapterName(chapterName.getText());
            String shipChapterWorld = business.parseChapterWorld(chapterWorld.getText());

            SpaceMarineRaw spaceMarineRaw = new SpaceMarineRaw(
                    shipName,
                    new Coordinates(shipX, shipY),
                    LocalDateTime.now(),
                    shipHealth,
                    shipAchievements,
                    shipWeaponType,
                    shipMeleeWeapon,
                    new Chapter(shipChapterName, shipChapterWorld)
            );
            resultMarine = spaceMarineRaw;
            System.out.println("--Мы тута!");
            editSpaceShipStage.close();
            //clientLauncher.changeScene("Table.fxml");

        } catch (IncorrectCoordinateXException e){
            UIOutputer.errorAlert("incorrectCoordinateXException");
        } catch(EmptyCoordinateYException e){
            UIOutputer.errorAlert("emptyCoordinateYException");
        } catch (EmptyChapterNameException e){
            UIOutputer.errorAlert("emptyChapterNameException");
        } catch (EmptyHealthException e){
            UIOutputer.errorAlert("emptyHealthException");
        } catch (IncorrectHealthException e){
            UIOutputer.errorAlert("incorrectHealthException");
        } catch (IncorrectCoordinateYException e){
            UIOutputer.errorAlert("incorrectCoordinateYException");
        } catch (EmptyChapterWorldException e){
            UIOutputer.errorAlert("emptyChapterWorldException");
        } catch (EmptyNameException e){
            UIOutputer.errorAlert("emptyNameException");
        } catch (NumberFormatHealthException e){
            UIOutputer.errorAlert("numberFormatHealthException");
        } catch (NumberFormatCoordinateXException e){
            UIOutputer.errorAlert("numberFormatCoordinateXException");
        } catch (NumberFormatCoordinateYException e) {
            UIOutputer.errorAlert("numberFormatCoordinateYException");
        }
    }

    public void initializeAll() {

        ObservableList<Weapon> weaponObservableList = FXCollections.observableArrayList(
                Weapon.BOLT_RIFLE,
                Weapon.FLAMER,
                Weapon.MELTAGUN,
                Weapon.MISSILE_LAUNCHER,
                Weapon.MULTI_MELTA
        );

        weaponType.getItems().addAll(weaponObservableList);
        weaponType.setValue(Weapon.FLAMER);

        ObservableList<MeleeWeapon> meleeWeaponObservableList = FXCollections.observableArrayList(
                MeleeWeapon.CHAIN_AXE,
                MeleeWeapon.LIGHTING_CLAW,
                MeleeWeapon.MANREAPER,
                MeleeWeapon.POWER_BLADE,
                MeleeWeapon.POWER_FIST
        );

        meleeWeapon.getItems().addAll(meleeWeaponObservableList);
        meleeWeapon.setValue(MeleeWeapon.CHAIN_AXE);

        if(lastNeedCommand != null && lastNeedCommand.equals("update")){
            id.setVisible(true);
            weaponType.getItems().addAll(Weapon.EMPTY);
            meleeWeapon.getItems().addAll(MeleeWeapon.EMPTY);
            if(graphId != null){
                id.setText(graphId.toString());
            }
        }
    }

    public void bindAppLanguage(){
        editTitle.textProperty().bind(resourceFactory.getStringBinding("editTitle"));
        idLabel.textProperty().bind(resourceFactory.getStringBinding("IDLabel"));
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("nameLabel"));
        creationDateLabel.textProperty().bind(resourceFactory.getStringBinding("creationDateLabel"));
        healthLabel.textProperty().bind(resourceFactory.getStringBinding("healthLabel"));
        achievementsLabel.textProperty().bind(resourceFactory.getStringBinding("achievementsLabel"));
        weaponTypeLabel.textProperty().bind(resourceFactory.getStringBinding("weaponTypeLabel"));
        meleeWeaponLabel.textProperty().bind(resourceFactory.getStringBinding("meleeWeaponLabel"));
        xLabel.textProperty().bind(resourceFactory.getStringBinding("XLabel"));
        yLabel.textProperty().bind(resourceFactory.getStringBinding("YLabel"));
        chapterNameLabel.textProperty().bind(resourceFactory.getStringBinding("chapterNameLabel"));
        chapterWorldLabel.textProperty().bind(resourceFactory.getStringBinding("chapterWorldLabel"));
        creatorLabel.textProperty().bind(resourceFactory.getStringBinding("creatorLabel"));
        btn_send.textProperty().bind(resourceFactory.getStringBinding("editSendButton"));

    }

    public void updateSpaceShip() {
        try{

            String shipName = null;
            if(!name.getText().isEmpty()) shipName = business.parseName(name.getText());
            Float shipHealth = null;
            if(!health.getText().isEmpty()) shipHealth = business.parseHealth(health.getText());
            String shipAchievements = null;
            if(!achievements.getText().isEmpty()) shipAchievements = business.parseAchievements(achievements.getText());
            Weapon shipWeaponType = null;
            if(!weaponType.getValue().equals(Weapon.EMPTY)) shipWeaponType = weaponType.getValue();
            MeleeWeapon shipMeleeWeapon = null;
            if(!meleeWeapon.getValue().equals(MeleeWeapon.EMPTY)) shipMeleeWeapon = meleeWeapon.getValue();
            long shipX = 992;
            if(!x.getText().isEmpty()) shipX = business.parseX(x.getText());
            Double shipY = null;
            if(!y.getText().isEmpty()) shipY = business.parseY(y.getText());
            String shipChapterName = null;
            if(!chapterName.getText().isEmpty()) shipChapterName = business.parseChapterName(chapterName.getText());
            String shipChapterWorld = null;
            if(!chapterWorld.getText().isEmpty()) shipChapterWorld = business.parseChapterWorld(chapterWorld.getText());

            SpaceMarineRaw spaceMarineRaw = new SpaceMarineRaw(
                    shipName,
                    new Coordinates(shipX, shipY),
                    LocalDateTime.now(),
                    shipHealth,
                    shipAchievements,
                    shipWeaponType,
                    shipMeleeWeapon,
                    new Chapter(shipChapterName, shipChapterWorld)
            );

            resultMarine = spaceMarineRaw;
            editSpaceShipStage.close();

        }
        catch (IncorrectCoordinateXException e){
            UIOutputer.errorAlert("incorrectCoordinateXException");
        } catch(EmptyCoordinateYException e){
            UIOutputer.errorAlert("emptyCoordinateYException");
        } catch (EmptyChapterNameException e){
            UIOutputer.errorAlert("emptyChapterNameException");
        } catch (EmptyHealthException e){
            UIOutputer.errorAlert("emptyHealthException");
        } catch (IncorrectHealthException e){
            UIOutputer.errorAlert("incorrectHealthException");
        } catch (IncorrectCoordinateYException e){
            UIOutputer.errorAlert("incorrectCoordinateYException");
        } catch (EmptyChapterWorldException e){
            UIOutputer.errorAlert("emptyChapterWorldException");
        } catch (EmptyNameException e){
            UIOutputer.errorAlert("emptyNameException");
        } catch (NumberFormatHealthException e){
            UIOutputer.errorAlert("numberFormatHealthException");
        } catch (NumberFormatCoordinateXException e){
            UIOutputer.errorAlert("numberFormatCoordinateXException");
        } catch (NumberFormatCoordinateYException e) {
            UIOutputer.errorAlert("numberFormatCoordinateYException");
        }

    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindAppLanguage();
    }

}
