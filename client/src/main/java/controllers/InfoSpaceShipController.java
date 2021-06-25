package controllers;

import data.SpaceMarine;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.ObservableResourceFactory;

public class InfoSpaceShipController {

    @FXML
    private Label informationTitle;

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
    private Label id;

    @FXML
    private Label name;

    @FXML
    private Label creationDate;

    @FXML
    private Label health;

    @FXML
    private Label achievements;

    @FXML
    private Label weaponType;

    @FXML
    private Label meleeWeapon;

    @FXML
    private Label x;

    @FXML
    private Label y;

    @FXML
    private Label chapterName;

    @FXML
    private Label chapterWorld;

    @FXML
    private Label creator;

    private SpaceMarine spaceMarine;
    private ObservableResourceFactory resourceFactory;

    public void setSpaceMarine(SpaceMarine spaceMarine) {
        this.spaceMarine = spaceMarine;
    }


    public void initializeAll(){

            if(spaceMarine != null){
                id.setText(spaceMarine.getId().toString());
                name.setText(spaceMarine.getName());
                creationDate.setText(spaceMarine.getCreationDate().toString());
                health.setText(spaceMarine.getHealth().toString());
                achievements.setText(spaceMarine.getAchievements());
                weaponType.setText(spaceMarine.getWeaponType().toString());
                meleeWeapon.setText(spaceMarine.getMeleeWeapon().toString());
                x.setText(Long.toString(spaceMarine.getCoordinates().getX()));
                y.setText(spaceMarine.getCoordinates().getY().toString());
                chapterName.setText(spaceMarine.getChapterName());
                chapterWorld.setText(spaceMarine.getChapterWorld());
                creator.setText(spaceMarine.getCreator().getUsername());
            }


    }

    public void bindAppLanguage(){
        informationTitle.textProperty().bind(resourceFactory.getStringBinding("infoTitle"));
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
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindAppLanguage();
    }




}
