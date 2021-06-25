package utils;

import data.*;
import exceptions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Validation + making a request class
 * @author NastyaBordun
 * @version 1.1
 */

public class Business {

    private HashMap<String, String> convertCommands;

    public Business() {
        convertCommands = new HashMap<>();
        convertCommands.put("Add", "add");
        convertCommands.put("Ascending weapon type", "print_field_ascending_weapon_type");
        convertCommands.put("Clear", "clear");
        convertCommands.put("Descending achievements", "print_field_descending_achievements");
        convertCommands.put("Execute script", "execute_script");
        convertCommands.put("Filter achievements", "filter_starts_with_achievements");
        convertCommands.put("Info", "info");
        convertCommands.put("Remove", "remove_by_id");
        convertCommands.put("Remove greater", "remove_greater");
        convertCommands.put("Remove lower", "remove_lower");
        convertCommands.put("Reorder", "reorder");
        convertCommands.put("Show", "show");
        convertCommands.put("Update", "update");
    }


    public ArgumentState analyzeCommand(String command) throws IncorrectCommandException {
        if(command == null) throw new IncorrectCommandException("Введена некорректная команда");
        switch (command) {
            case "help":
            case "info":
            case "clear":
            case "reorder":
            case "print_field_ascending_weapon_type":
            case "print_field_descending_achievements":
            case "show":
                return ArgumentState.OK;
            case "remove_greater":
            case "remove_lower":
            case "add":
            case "update":
                return ArgumentState.NEED_OBJ;
            case "filter_starts_with_achievements":
            case "remove_by_id":
                return ArgumentState.NEED_ARG;
            case "execute_script":
                return ArgumentState.NEED_FILE;
            default:
                throw new IncorrectCommandException("Введена некорректная команда");
        }
    }

    public String getConvertCommand(String commandNotConvert) throws IncorrectCommandException {
        String command = convertCommands.get(commandNotConvert);
        if(command == null) throw new IncorrectCommandException("Введена некорректная команда");
        return command;
    }

    public Integer parseId(String strId) throws IncorrectIdException {
        Integer id = 0;
        try{
            if(strId.isEmpty()) throw new IncorrectIdException("Поле id не может быть пустым");
            id = Integer.parseInt(strId);
            if(id <= 0) throw new IncorrectIdException("Поле id должно быть >= 1");
            return id;
        }
        catch (NumberFormatException e){
            throw new IncorrectIdException("Введено некорректное поле ID");
        }

    }

    public String parseName(String name) throws EmptyNameException {
        if(name.length() == 0) throw new EmptyNameException("Поле name не может быть пустым");
        return name;
    }


    public Float parseHealth(String strHealth) throws EmptyHealthException, IncorrectHealthException, NumberFormatHealthException {
        Float health = 0F;
        try{
            if(strHealth.length() != 0){
                if (Float.parseFloat(strHealth) <= 0) {
                    throw new IncorrectHealthException("Поле health должно быть больше 0");
                }
                health = Float.parseFloat(strHealth);
            }
            else throw new EmptyHealthException("Поле health не может быть пустым");
        }
        catch (NumberFormatException e){
            throw new NumberFormatHealthException("Введено некорректное поле health");
        }
        return health;
    }

    public String parseAchievements(String achievements){
        return achievements;
    }

    public long parseX(String strX) throws IncorrectCoordinateXException, NumberFormatCoordinateXException {
        long coordinateX = 0;
        try{
            if (Math.abs(Long.parseLong(strX)) > 991) {
                throw new IncorrectCoordinateXException("Координата x поля coordinates не может быть больше 991 по модулю");
            }
            coordinateX = Long.parseLong(strX);
        }
        catch (NumberFormatException e){
            throw new NumberFormatCoordinateXException("Введено некорректное поле X");
        }
        return coordinateX;
    }

    public Double parseY(String strY) throws EmptyCoordinateYException, IncorrectCoordinateYException, NumberFormatCoordinateYException {
        Double coordinateY = 0D;
        try{
            if (strY.length() == 0) {
                throw new EmptyCoordinateYException("Координата Y поля coordinates не может быть пустой");
            }
            if (Math.abs(Double.parseDouble(strY)) > 767) {
                throw new IncorrectCoordinateYException("Координата Y поля coordinates не может быть больше 767 по модулю");
            }
            coordinateY = Double.parseDouble(strY);
        }
        catch (NumberFormatException e){
            throw new NumberFormatCoordinateYException("Введено некорректное поле Y");
        }
        return coordinateY;
    }

    public String parseChapterName(String chapterName) throws EmptyChapterNameException {
        if(chapterName.length() == 0){
            throw new EmptyChapterNameException("Поле name класса chapter не может быть пустым");
        }
        return chapterName;
    }

    public String parseChapterWorld(String chapterWorld) throws EmptyChapterWorldException {
        if(chapterWorld.length() == 0){
            throw new EmptyChapterWorldException("Поле world класса chapter не может быть пустым");
        }
        return chapterWorld;
    }



}
