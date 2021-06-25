package utils;

import answers.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;

/**
 * The sender
 * @author NastyaBordun
 * @version 1.1
 */

public class Sender {

    private SocketAddress socketAddress;
    private DatagramChannel channel;

    public Sender(SocketAddress socketAddress, DatagramChannel datagramChannel){
        this.socketAddress = socketAddress;
        this.channel = datagramChannel;
    }

    public void sendResponse(Response response){
        try{
            System.out.println("Response: " + response.toString());
            ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(byteArrayOutputStream1);
            objectOutputStream1.writeObject(response);

            ConParamSession conParamSession = new ConParamSession(Server.BUF_SZ);
            conParamSession.sa = socketAddress;
            conParamSession.resp.put(byteArrayOutputStream1.toByteArray());

            objectOutputStream1.flush();
            byteArrayOutputStream1.flush();
            conParamSession.resp.flip();

            channel.send(conParamSession.resp, conParamSession.sa);

            conParamSession.resp.clear();
            objectOutputStream1.close();
            byteArrayOutputStream1.close();

            ServerLauncher.logger.log(Level.INFO, "Ответ отправлен");
        }
        catch (IOException e){
            ServerLauncher.logger.log(Level.SEVERE, "Ошибка при отправке ответа");
        }
    }
}
