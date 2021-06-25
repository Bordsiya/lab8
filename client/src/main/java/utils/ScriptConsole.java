package utils;

import javafx.scene.control.Alert;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Working environment for command line reading
 * @author NastyaBordun
 * @version 1.1
 */

public class ScriptConsole {

    /**
     * Variable to identify the need for further work
     */
    private boolean work;

    /**
     * For requests handling {@link Client}
     */
    private Client client;

    /**
     * For adding scriptModes {@link AskManager}
     */
    private AskManager askManager;

    /**
     * Constructor for class
     * @param client For requests handling {@link Client}
     * @param askManager For adding scriptModes {@link AskManager}
     */
    public ScriptConsole(Client client, AskManager askManager){
        this.work = true;
        this.client = client;
        this.askManager = askManager;
    }


    /**
     * Setting working state
     * @param work needful state
     */
    public void setWork(boolean work){
        this.work = work;
    }

    /**
     * Work with a script
     *
     */
    public void scriptMode(File scriptFile){
        try {
            FileInputStream file = new FileInputStream(scriptFile);
            BufferedInputStream bf = new BufferedInputStream(file);
            BufferedReader r = new BufferedReader(new InputStreamReader(bf, StandardCharsets.UTF_8));
            String line = r.readLine().trim();
            while(line != null && this.work){
                Printer.println("Команда из скриптов: " + line.trim());
                if(askManager.getBf2() != r || askManager.getBf2() == null) askManager.addScriptReader(r);
                System.out.println("/" + line.trim());
                client.handleScript(line.trim());

                line = r.readLine();
            }
        } catch (FileNotFoundException e) {
            Printer.printError("Файл с таким именем не существует");
            UIOutputer.errorAlert("fileNotFoundException");
        } catch (IOException e) {
            Printer.printError("Ошибка ввода/вывода");
            UIOutputer.errorAlert("ioeException");
        }

        setWork(true);
    }



}
