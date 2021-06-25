package utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

/**
 * Server
 * @author NastyaBordun
 * @version 1.1
 */
public class Server{

    private CommandManager commandManager;
    private Receiver receiver;
    private CollectionManager collectionManager;

    public static final int BUF_SZ = 16384;

    public Server(CommandManager commandManager, Receiver receiver, CollectionManager collectionManager){
        this.commandManager = commandManager;
        this.receiver = receiver;
        this.collectionManager = collectionManager;
    }

    /**
     * Server running
     */
    public void run() {
        try {
            Selector selector = Selector.open();
            DatagramChannel channel = DatagramChannel.open();
            InetSocketAddress isa = new InetSocketAddress(ServerLauncher.PORT);
            channel.socket().bind(isa);
            channel.configureBlocking(false);
            SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ);
            clientKey.attach(new ConParam(BUF_SZ));
            while(true){
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keys = selectedKeys.iterator();
                while(keys.hasNext()){
                    SelectionKey key = keys.next();
                    if (!key.isValid()) {
                        continue;
                    }
                    if(key.isReadable()){
                        ServerLauncher.logger.log(Level.INFO, "Найден запрос на чтение");
                        synchronized (key){
                            DatagramChannel chan = receiver.getDatagramChannel(key);
                            ConParam con = receiver.readConParam(chan, key);
                            Thread readThread = new Thread(new MultithreadReader(con, chan, receiver, commandManager, collectionManager));
                            readThread.start();
                        }


                    }
                    keys.remove();
                }
            }

        }
        catch (IOException e) {
            ServerLauncher.logger.log(Level.SEVERE, "Сервер упал");
        }
    }


}
