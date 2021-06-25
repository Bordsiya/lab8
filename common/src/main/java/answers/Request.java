package answers;

import data.User;

import java.io.Serializable;

/**
 * Request form
 * @author NastyaBordun
 * @version 1.1
 */
public class Request implements Serializable {
    private String commandName = "";
    private String commandStringArg = "";
    private Serializable commandObjectArg = null;
    private User user;

    public Request(String commandName, String commandStringArg, Serializable commandObjectArg, User user){
        this.commandName = commandName;
        this.commandStringArg = commandStringArg;
        this.commandObjectArg = commandObjectArg;
        this.user = user;
    }

    public Request(String commandName, String commandStringArg, User user){
        this.commandName = commandName;
        this.commandStringArg = commandStringArg;
        this.commandObjectArg = null;
        this.user = user;
    }

    public Request(String commandName, User user){
        this.commandName = commandName;
        this.commandStringArg = "";
        this.commandObjectArg = null;
        this.user = user;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandStringArg() {
        return commandStringArg;
    }

    public Serializable getCommandObjectArg() {
        return commandObjectArg;
    }

    public User getUser(){
        return user;
    }

    public boolean isEmpty(){
        return commandName.isEmpty() && commandStringArg.isEmpty() && commandObjectArg == null;
    }

    @Override
    public String toString() {
        return "Запрос:\n" + "Название команды: " + this.getCommandName() + "\nТекстовый аргумент: "
                + this.getCommandStringArg() + "\nАргумент-объект: " + this.commandObjectArg + "\nПользователь: "
                + this.getUser().toString() + "\n";
    }
}
