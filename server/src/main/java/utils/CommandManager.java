package utils;

import commands.ICommand;
import data.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for command announcement
 * @author NastyaBordun
 * @version 1.1
 */

public class CommandManager {

    private Map<String, ICommand> commands = new HashMap<>();

    public CommandManager(ICommand... commandsValues){
        for(ICommand com : commandsValues){
            if(com != null) commands.put(com.getName(), com);
        }
    }

    public Map<String, ICommand> getCommands(){
        return this.commands;
    }

    public void addCommand(ICommand command){
        commands.put(command.getName(), command);
    }

    public boolean executeCommand(String commandName, String str, Object arg, User user){
        //System.out.println("///" + commandName + " " + commands.get(commandName));
        if(commandExist(commandName)){
            return commands.get(commandName).execute(str, arg, user);
        }
        else return false;
    }

    public boolean commandExist(String commandName){
        if(commands.containsKey(commandName)) return true;
        else return false;
    }

}
