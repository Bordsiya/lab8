import utils.Client;
import utils.ClientLauncher;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Main app
 * @author NastyaBordun
 * @version 1.1
 */
public class ApplicationClient {
    public static void main(String[] args){
        ClientLauncher clientLauncher = new ClientLauncher();
        clientLauncher.launchClient(args);
    }
}
