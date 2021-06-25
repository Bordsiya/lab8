package commands;

/**
 * Abstract realisation of interface {@link ICommand}
 * @author NastyaBordun
 * @version 1.1
 */

public abstract class AbstractCommand implements ICommand{

    private String name;
    private String description;

    public AbstractCommand(String name, String description){
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
