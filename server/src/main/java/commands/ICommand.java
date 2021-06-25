package commands;

import data.User;

/**
 * Interface for commands
 * @author NastyaBordun
 * @version 1.1
 */

public interface ICommand {

    boolean execute(String str, Object arg, User user);

    String getName();

    String getDescription();
}
