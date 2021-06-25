package controllers;

import animations.Grow;
import answers.Response;
import data.*;
import exceptions.IncorrectCommandException;
import exceptions.NotYourSpaceShipException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.*;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Stack;

public class TableController {

    private Client client;
    private Business business;
    private File scriptFile = null;

    private Stage editStage;
    private EditSpaceShipController editSpaceShipController;
    private Stage infoStage;
    private InfoSpaceShipController infoSpaceShipController;

    public void setClient(Client client){
        this.client = client;
    }
    public void setBusiness(Business business){
        this.business = business;
    }


    public void setEditStage(Stage editStage) {
        this.editStage = editStage;
    }

    public void setEditSpaceShipController(EditSpaceShipController editSpaceShipController) {
        this.editSpaceShipController = editSpaceShipController;
    }

    public void setInfoStage(Stage infoStage) {
        this.infoStage = infoStage;
    }

    public void setInfoSpaceShipController(InfoSpaceShipController infoSpaceShipController) {
        this.infoSpaceShipController = infoSpaceShipController;
    }

    @FXML
    private Label login;

    @FXML
    private Label exit;

    @FXML
    private ChoiceBox<String> commandBox;

    @FXML
    private TextField arg;

    @FXML
    private Button btn_send;

    @FXML
    private ImageView info;

    @FXML
    private TableView<SpaceMarine> table;

    @FXML
    private TableColumn<SpaceMarine, Integer> id;

    @FXML
    private TableColumn<SpaceMarine, String> name;

    @FXML
    private TableColumn<SpaceMarine, LocalDateTime> creationDate;

    @FXML
    private TableColumn<SpaceMarine, Float> health;

    @FXML
    private TableColumn<SpaceMarine, String> achievements;

    @FXML
    private TableColumn<SpaceMarine, Weapon> weaponType;

    @FXML
    private TableColumn<SpaceMarine, MeleeWeapon> meleeWeapon;

    @FXML
    private TableColumn<SpaceMarine, Long> x;

    @FXML
    private TableColumn<SpaceMarine, Double> y;

    @FXML
    private TableColumn<SpaceMarine, String> chapterName;

    @FXML
    private TableColumn<SpaceMarine, String> chapterWorld;

    @FXML
    private TableColumn<SpaceMarine, String> creator;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label tableTitle;

    @FXML
    private AnchorPane canvas;

    private HashMap<String, Color> userColorMap;
    private HashMap<Shape, Integer> shapeIdMap;
    private HashMap<Integer, Text> textMap;
    private HashMap<String, Locale> localeMap;
    private ObservableResourceFactory resourceFactory;
    private int index = -1;
    public static ObservableList<SpaceMarine> spaceMarines;
    private Thread update = new Thread(new Updater());

    public void toExitClick(MouseEvent mouseEvent){
        System.exit(0);
    }

    public void getSelected(MouseEvent mouseEvent){
        index = table.getSelectionModel().getSelectedIndex();
    }

    public void fileClicked(MouseEvent mouseEvent){
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File("./"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            scriptFile = selectedFile;
        }
    }

    public void infoClicked(MouseEvent mouseEvent){
        makeQuestionRequest("help", "", null);
    }

    public void toSendPress(ActionEvent actionEvent) {
        String argument = arg.getText();
        Response response = null;
        try{
            String command = business.getConvertCommand(commandBox.getValue());
            if(business.analyzeCommand(command).equals(ArgumentState.OK)){
                makeQuestionRequest(command, "", null);
            }
            else if(business.analyzeCommand(command).equals(ArgumentState.NEED_ARG)){
                makeQuestionRequest(command, argument, null);
            }
            else if(business.analyzeCommand(command).equals(ArgumentState.NEED_OBJ)){
                editSpaceShipController.setLastNeedCommand(command);
                editSpaceShipController.initializeAll();
                editStage.showAndWait();
                SpaceMarineRaw sendingSpaceMarine = editSpaceShipController.getResultMarine();
                if(command.equals("update")){
                    makeQuestionRequest(command, argument, sendingSpaceMarine);
                }
                else makeQuestionRequest(command, "", sendingSpaceMarine);
                editSpaceShipController.clearFields();
            }
            else if(business.analyzeCommand(command).equals(ArgumentState.NEED_FILE)){
                if(scriptFile != null){
                    client.handleScript(command + " " + scriptFile.getName());
                }
                else throw new ScriptException("Не выбран файл");
            }
        }
        catch (IncorrectCommandException e){
            UIOutputer.errorAlert("incorrectCommandException");
        } catch (ScriptException e){
            UIOutputer.errorAlert("scriptException");
        }

    }

    public void initializeAll(){
        login.setText(client.getUser().getUsername());

        userColorMap = new HashMap<>();
        userColorMap.put(client.getUser().getUsername(), client.getUserColor());
        shapeIdMap = new HashMap<>();
        textMap = new HashMap<>();

        Image map = new Image(getClass().getResourceAsStream("/pictures/map.jpeg"));
        ImageView mapView = new ImageView(map);
        mapView.fitWidthProperty().bind(canvas.widthProperty());
        mapView.fitHeightProperty().bind(canvas.heightProperty());
        canvas.getChildren().add(mapView);


        ObservableList<String> commandsList = FXCollections.observableArrayList(
                "Add",
                "Ascending weapon type",
                "Clear",
                "Descending achievements",
                "Execute script",
                "Filter achievements",
                "Info",
                "Remove",
                "Remove greater",
                "Remove lower",
                "Show",
                "Update");

        commandBox.getItems().addAll(commandsList);
        commandBox.setValue("Info");

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        health.setCellValueFactory(new PropertyValueFactory<>("health"));
        achievements.setCellValueFactory(new PropertyValueFactory<>("achievements"));
        weaponType.setCellValueFactory(new PropertyValueFactory<>("weaponType"));
        meleeWeapon.setCellValueFactory(new PropertyValueFactory<>("meleeWeapon"));
        x.setCellValueFactory(new PropertyValueFactory<>("x"));
        y.setCellValueFactory(new PropertyValueFactory<>("y"));
        chapterName.setCellValueFactory(new PropertyValueFactory<>("chapterName"));
        chapterWorld.setCellValueFactory(new PropertyValueFactory<>("chapterWorld"));
        creator.setCellValueFactory(new PropertyValueFactory<>("creatorName"));

        localeMap = new HashMap<>();
        localeMap.put("English", new Locale("en", "EN"));
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Svenska", new Locale("sh", "SH"));
        localeMap.put("Íslenska", new Locale("isl", "ISL"));
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));

        update.start();

    }

    public void bindAppLanguage(){
        resourceFactory.setResources(ResourceBundle.getBundle(
                ClientLauncher.BUNDLE, localeMap.get(languageComboBox.getSelectionModel().getSelectedItem())));
        id.textProperty().bind(resourceFactory.getStringBinding("tableIdColumn"));
        name.textProperty().bind(resourceFactory.getStringBinding("tableNameColumn"));
        creationDate.textProperty().bind(resourceFactory.getStringBinding("tableCreationDateColumn"));
        health.textProperty().bind(resourceFactory.getStringBinding("tableHealthColumn"));
        achievements.textProperty().bind(resourceFactory.getStringBinding("tableAchievementsColumn"));
        weaponType.textProperty().bind(resourceFactory.getStringBinding("tableWeaponTypeColumn"));
        meleeWeapon.textProperty().bind(resourceFactory.getStringBinding("tableMeleeWeaponColumn"));
        x.textProperty().bind(resourceFactory.getStringBinding("tableXColumn"));
        y.textProperty().bind(resourceFactory.getStringBinding("tableYColumn"));
        chapterName.textProperty().bind(resourceFactory.getStringBinding("tableChapterName"));
        chapterWorld.textProperty().bind(resourceFactory.getStringBinding("tableChapterWorld"));
        creator.textProperty().bind(resourceFactory.getStringBinding("tableCreatorColumn"));

        exit.textProperty().bind(resourceFactory.getStringBinding("tableExit"));
        tableTitle.textProperty().bind(resourceFactory.getStringBinding("tableTitle"));
        arg.promptTextProperty().bind(resourceFactory.getStringBinding("tableArgPromptText"));
        btn_send.textProperty().bind(resourceFactory.getStringBinding("tableSendButton"));
    }

    private void makeQuestionRequest(String command, String commandArg, Serializable commandObj){
        client.handle(command, commandArg, commandObj, client.getUser());
    }

    private Response makeRefreshRequest(String command, String commandArg, Serializable commandObj){
        return client.handleRefresh(command, commandArg, commandObj, client.getUser());
    }

    class Updater implements Runnable{

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Response response = makeRefreshRequest("show", "", null);
                if((response != null && spaceMarines != null && hasCollectionDifferences(response.getCollection()))
                            || (response != null && spaceMarines == null)){
                        ObservableList<SpaceMarine> marinesList = FXCollections.observableArrayList(response.getCollection());
                        table.setItems(marinesList);
                        spaceMarines = marinesList;
                        ObservableList<SpaceMarine> observableList = spaceMarines;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                drawSpaceShips(observableList);
                            }
                        });
                    }
                }

        }
    }

    public boolean hasCollectionDifferences(Stack<SpaceMarine> stack){
        if(stack.size() != spaceMarines.size()) return true;
        for(int i = 0; i < stack.size(); i++){
            if(!stack.get(i).equals(spaceMarines.get(i))) return true;
        }
        return false;
    }

    public void drawCircle(long x, double y, float radius, Color color, Integer id){
        float circleRadius = Math.max(radius, 10);
        Circle circle = new Circle(circleRadius);
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(5);
        circle.setFill(color);
        circle.translateXProperty().bind(canvas.widthProperty().divide(2).add(x));
        circle.translateYProperty().bind(canvas.heightProperty().divide(2).add(y));
        circle.setOnMouseClicked(this::spaceShipClicked);

        canvas.getChildren().add(circle);
        shapeIdMap.put(circle, id);

        Text text = new Text(id.toString());
        text.setFill(Color.WHITE);
        text.setFont(Font.font(null, FontWeight.BOLD, 18));
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(0.8);
        text.translateXProperty().bind(canvas.widthProperty().divide(2).add(x));
        text.translateYProperty().bind(canvas.heightProperty().divide(2).add(y));
        textMap.put(id, text);

        canvas.getChildren().add(text);

        Grow circleGrow = new Grow(circle);
        circleGrow.playAnimation();
        Grow textGrow = new Grow(text);
        textGrow.playAnimation();
    }

    public void drawSpaceShips(ObservableList<SpaceMarine> collection){
        shapeIdMap.keySet().forEach(s -> canvas.getChildren().remove(s));
        shapeIdMap.clear();
        textMap.keySet().forEach(s -> canvas.getChildren().remove(textMap.get(s)));
        textMap.clear();
        if(collection != null){
            ObservableList<SpaceMarine> marinesList = FXCollections.observableArrayList(collection);
            for (SpaceMarine sp: marinesList){
                Integer id = sp.getId();
                long x = sp.getCoordinates().getX();
                Double y = sp.getCoordinates().getY();
                Float size = sp.getHealth();
                if(!userColorMap.containsKey(sp.getCreator().getUsername())){
                    userColorMap.put(sp.getCreator().getUsername(),
                            Color.color((float)Math.random(), (float)Math.random(), (float)Math.random()));
                }
                Color color = userColorMap.get(sp.getCreator().getUsername());
                drawCircle(x, y, size/3, color, id);
            }
        }
    }

    public void spaceShipClicked(MouseEvent mouseEvent) {

            if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                try {
                    Shape shape = (Shape) mouseEvent.getSource();
                    Integer searchId = shapeIdMap.get(shape);
                    for (SpaceMarine sp : spaceMarines) {
                        if (sp.getId().equals(searchId)) {
                            if (sp.getCreator().getUsername().equals(client.getUser().getUsername())) {
                                editSpaceShipController.setGraphId(sp.getId());
                                editSpaceShipController.setLastNeedCommand("update");
                                editSpaceShipController.initializeAll();
                                editStage.showAndWait();
                                SpaceMarineRaw sendingSpaceMarine = editSpaceShipController.getResultMarine();
                                makeQuestionRequest("update", sp.getId().toString(), sendingSpaceMarine);
                                editSpaceShipController.clearFields();
                            } else {
                                throw new NotYourSpaceShipException("Вы можете редактировать только свои корабли");
                            }
                        }
                    }
                }
                catch (NotYourSpaceShipException e){
                    UIOutputer.errorAlert("notYourSpaceShipException");
                }
            }
            else if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                Shape shape = (Shape) mouseEvent.getSource();
                Integer searchMarineId = shapeIdMap.get(shape);
                SpaceMarine checkSpaceMarine = null;
                for(SpaceMarine sp: spaceMarines){
                    if(sp.getId().equals(searchMarineId)){
                        checkSpaceMarine = sp;
                        break;
                    }
                }
                infoSpaceShipController.setSpaceMarine(checkSpaceMarine);
                infoSpaceShipController.initializeAll();
                infoStage.showAndWait();
            }



        }

        public void initLangs(ObservableResourceFactory resourceFactory) {
            this.resourceFactory = resourceFactory;
            for (String localeName : localeMap.keySet()) {
                if (localeMap.get(localeName).equals(resourceFactory.getResources().getLocale()))
                    languageComboBox.getSelectionModel().select(localeName);
            }
            if (languageComboBox.getSelectionModel().getSelectedItem().isEmpty()) {
                languageComboBox.getSelectionModel().selectFirst();
            }
            languageComboBox.setOnAction((event ->
                    resourceFactory.setResources(ResourceBundle.getBundle(
                            ClientLauncher.BUNDLE, localeMap.get(languageComboBox.getValue())
                    ))));
            bindAppLanguage();
        }





}
