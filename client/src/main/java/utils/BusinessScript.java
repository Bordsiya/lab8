package utils;

import answers.Request;
import data.*;
import exceptions.*;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Stack;

public class BusinessScript {
    /**
     * Name of scripts which are executing now
     */
    private final Stack<File> scriptsStack;

    /**
     * Class providing getting correct information from the user {@link AskManager}
     */
    private AskManager askManager;

    /**
     * For special commands
     */
    private ScriptConsole scriptConsole = null;


    /**
     * Constructor for class
     * @param askManager Class providing getting correct information from the user {@link AskManager}
     */
    public BusinessScript(AskManager askManager){
        this.askManager = askManager;
        scriptsStack = new Stack<>();
    }

    /**
     * Adding {@link ScriptConsole} object to class
     * @param scriptConsole For special commands
     */
    public void addConsole(ScriptConsole scriptConsole){
        this.scriptConsole = scriptConsole;
    }


    /**
     * Refactoring string before the execution
     * @param commandArr command line
     * @return refactored String
     * @throws IncorrectCommandException undefined command
     */
    private String refactorString(String[] commandArr) throws IncorrectCommandException {
        if(commandArr.length == 0) throw new IncorrectCommandException("Введена некорректная команда");
        String line;
        if(commandArr.length == 1){
            line = "";
        }
        else{
            line = commandArr[1];
        }
        return line;
    }

    /**
     * Making request to Server
     * @param command user command
     * @return {@link Request} request to Server
     * @throws IncorrectCommandException Incorrect command
     */
    public Request makeRequest(String command, User user) throws IncorrectCommandException {
        String[] commandArr = command.trim().split(" ", 2);

        String line = refactorString(commandArr);
        ArgumentState argumentState = analyzeCommand(commandArr[0], line);
        if(argumentState == ArgumentState.ERROR) return null;
        try {
            SpaceMarineRaw spaceMarineRaw = null;
            switch (argumentState) {
                case ADD_OBJECT: {
                    spaceMarineRaw = addSpaceMarine();
                    return new Request(commandArr[0], line, spaceMarineRaw, user);
                }
                case UPDATE_OBJECT: {
                    spaceMarineRaw = updateSpaceMarine();
                    return new Request(commandArr[0], line, spaceMarineRaw, user);
                }
                case SCRIPT_MODE: {
                    File scriptFile = new File(line);
                    if(!scriptFile.exists()) throw new FileNotFoundException("Файл с таким именем не существует");
                    if (!scriptsStack.isEmpty() && scriptsStack.search(scriptFile) != -1){
                        throw new RecursionScriptException("Рекурсивный вызов файла-скрипта");
                    }
                    scriptsStack.push(scriptFile);
                    Printer.println("---Начато выполнение скрипта---");
                    scriptConsole.scriptMode(scriptFile);
                    scriptsStack.pop();
                    break;
                }
                case EXIT:
                    scriptConsole.setWork(false);
            }
        }
        catch (IncorrectScriptInputException e){
            UIOutputer.errorAlert("incorrectScriptInputException");
            return null;
        } catch (RecursionScriptException e){
            UIOutputer.errorAlert("recursionScriptException");
            return null;
        } catch (FileNotFoundException e){
            UIOutputer.errorAlert("fileNotFoundException");
            return null;
        } catch (EmptyNameException e){
            UIOutputer.errorAlert("emptyNameException");
            return null;
        } catch (EmptyCoordinateYException e){
            UIOutputer.errorAlert("emptyCoordinateYException");
            return null;
        } catch (NumberFormatCoordinateYException e){
            UIOutputer.errorAlert("numberFormatCoordinateYException");
            return null;
        } catch (IncorrectCoordinateYException e){
            UIOutputer.errorAlert("incorrectCoordinateYException");
            return null;
        } catch (IncorrectCoordinateXException e){
            UIOutputer.errorAlert("incorrectCoordinateXException");
            return null;
        } catch (NumberFormatCoordinateXException e){
            UIOutputer.errorAlert("numberFormatCoordinateXException");
            return null;
        } catch (IncorrectHealthException e){
            UIOutputer.errorAlert("incorrectHealthException");
            return null;
        } catch (NumberFormatHealthException e){
            UIOutputer.errorAlert("numberFormatHealthException");
            return null;
        } catch (EmptyHealthException e){
            UIOutputer.errorAlert("emptyHealthException");
            return null;
        } catch (IncorrectWeaponTypeException e){
            UIOutputer.errorAlert("incorrectWeaponTypeException");
            return null;
        } catch (IncorrectMeleeWeaponException e){
            UIOutputer.errorAlert("incorrectMeleeWeaponException");
            return null;
        } catch (EmptyChapterWorldException e){
            UIOutputer.errorAlert("emptyChapterWorldException");
            return null;
        } catch (EmptyChapterNameException e){
            UIOutputer.errorAlert("emptyChapterNameException");
            return null;
        } catch (IncorrectAnswerException e){
            UIOutputer.errorAlert("incorrectAnswerException");
            return null;
        }
        return new Request(commandArr[0], line, user);
    }

    /**
     * Class for analyzing commands (what we need to add to request or number of arguments)
     * @param command command name
     * @param arg command argument
     * @return {@link ArgumentState}
     */
    public ArgumentState analyzeCommand(String command, String arg){
        try {
            switch (command) {
                case "help":
                case "info":
                case "clear":
                case "print_field_ascending_weapon_type":
                case "print_field_descending_achievements":
                case "show":
                    if (arg.length() != 0) {
                        throw new WrongAmountOfElements("Неправильное количество аргументов в команде");
                    }
                    else return ArgumentState.OK;
                case "remove_greater":
                case "remove_lower":
                case "add":
                    if (arg.length() != 0) {
                        throw new WrongAmountOfElements("Неправильное количество аргументов в команде");
                    }
                    else return ArgumentState.ADD_OBJECT;
                case "update":
                    if (arg.length() == 0) {
                        throw new WrongAmountOfElements("Неправильное количество аргументов в команде");
                    }
                    else return ArgumentState.UPDATE_OBJECT;
                case "filter_starts_with_achievements":
                case "remove_by_id":
                    String [] commandArr1 = arg.trim().split(" ");
                    if (arg.length() == 0 || commandArr1.length != 1) {
                        throw new WrongAmountOfElements("Неправильное количество аргументов в команде");
                    }
                    else return ArgumentState.OK;
                case "execute_script":
                    if(arg.length() == 0){
                        throw new WrongAmountOfElements("Неправильное количество аргументов для команды");
                    }
                    else return ArgumentState.SCRIPT_MODE;
                case "exit":
                    if(arg.length() != 0){
                        throw new WrongAmountOfElements("Неправильное количество аргументов для команды");
                    }
                    else return ArgumentState.EXIT;
                default:
                    throw new IncorrectCommandException("Введена некорректная команда");
            }
        }
        catch (WrongAmountOfElements e){
            Printer.printError(e.getMessage());
            UIOutputer.errorAlert("wrongAmountOfElements");
            return ArgumentState.ERROR;
        } catch (IncorrectCommandException e){
            Printer.printError(e.getMessage());
            UIOutputer.errorAlert("incorrectCommandException");
            return ArgumentState.ERROR;
        }
    }

    /**
     * Class for creating {@link SpaceMarine} for request (add)
     * @return adding {@link SpaceMarine}
     * @throws IncorrectScriptInputException Something wrong with script
     */
    private SpaceMarineRaw addSpaceMarine() throws IncorrectScriptInputException, EmptyNameException, EmptyCoordinateYException, NumberFormatCoordinateYException, IncorrectCoordinateYException, IncorrectCoordinateXException, NumberFormatCoordinateXException, IncorrectHealthException, NumberFormatHealthException, EmptyHealthException, IncorrectWeaponTypeException, IncorrectMeleeWeaponException, EmptyChapterWorldException, EmptyChapterNameException {
        if(!scriptsStack.empty()){
            askManager.setInteractiveMode(false);
        }
        SpaceMarineRaw spaceMarineRaw = new SpaceMarineRaw(
                askManager.askName(),
                askManager.askCoordinates(),
                LocalDateTime.now(),
                askManager.askHealth(),
                askManager.askAchievements(),
                askManager.askWeaponType(),
                askManager.askMeleeWeapon(),
                askManager.askChapter()
        );
        askManager.setInteractiveMode(true);
        return spaceMarineRaw;
    }

    /**
     * Creating {@link SpaceMarine} for request (update)
     * @return updating {@link SpaceMarine}
     * @throws IncorrectScriptInputException Something wrong with script
     */
    private SpaceMarineRaw updateSpaceMarine() throws IncorrectScriptInputException, IncorrectAnswerException, EmptyNameException, NumberFormatCoordinateXException, IncorrectCoordinateXException, NumberFormatCoordinateYException, EmptyCoordinateYException, IncorrectCoordinateYException, IncorrectHealthException, NumberFormatHealthException, EmptyHealthException, IncorrectWeaponTypeException, IncorrectMeleeWeaponException, EmptyChapterNameException, EmptyChapterWorldException {
        if(!scriptsStack.empty()){
            askManager.setInteractiveMode(false);
        }
        String name = null;
        if(askManager.questionCheck("name")) name = askManager.askName();
        long x = 992;
        if(askManager.questionCheck("координата x")) x = askManager.askCoordinateX();
        Double y = null;
        if(askManager.questionCheck("координата y")) y = askManager.askCoordinateY();
        Float health = null;
        if(askManager.questionCheck("health")) health = askManager.askHealth();
        String achievements = null;
        if(askManager.questionCheck("achievements")) achievements = askManager.askAchievements();
        Weapon weaponType = null;
        if(askManager.questionCheck("weaponType")) weaponType = askManager.askWeaponType();
        MeleeWeapon meleeWeapon = null;
        if(askManager.questionCheck("meleeWeapon")) meleeWeapon = askManager.askMeleeWeapon();
        String chapterName = null;
        if(askManager.questionCheck("chapterName")) chapterName = askManager.askChapterName();
        String chapterWorld = null;
        if(askManager.questionCheck("chapterWorld")) chapterWorld = askManager.askChapterWorld();
        Coordinates coordinates = new Coordinates(x, y);
        Chapter chapter = new Chapter(chapterName, chapterWorld);
        SpaceMarineRaw spaceMarineRaw = new SpaceMarineRaw(
                name,
                coordinates,
                LocalDateTime.now(),
                health,
                achievements,
                weaponType,
                meleeWeapon,
                chapter
        );
        askManager.setInteractiveMode(true);
        return spaceMarineRaw;
    }
}
