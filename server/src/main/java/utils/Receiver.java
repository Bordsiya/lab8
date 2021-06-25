package utils;

import answers.Request;
import exceptions.NullConParamException;
import exceptions.NullDatagramChanelException;
import exceptions.NullSelectionKeyException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.logging.Level;

/**
 * The receiver
 * @author NastyaBordun
 * @version 1.1
 */

public class Receiver {


    public Receiver() {

    }


    public synchronized DatagramChannel getDatagramChannel(SelectionKey key) {
        try{
            if(key == null){
                throw new NullSelectionKeyException("SelectionKey не может быть null");
            }
            DatagramChannel chan = (DatagramChannel) key.channel();
            return chan;
        } catch (NullSelectionKeyException e){
            ServerLauncher.logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public synchronized ConParam readConParam(DatagramChannel chan, SelectionKey key){
        try{
            if(chan == null) throw new NullDatagramChanelException("Канал не может быть null");
            ConParam con = (ConParam) key.attachment();
            con.sa = chan.receive(con.req);
            return con;
        }
        catch (IOException e){
            ServerLauncher.logger.log(Level.SEVERE, "Нет запроса на чтение");
            return null;
        }
        catch (NullDatagramChanelException e){
            ServerLauncher.logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public synchronized Request getRequest(ConParam con) {
            try {
                if(con == null) throw new NullConParamException("ConParam не может быть null");
                con.req.flip();

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(con.req.array());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Request request = (Request) objectInputStream.readObject();
                //System.out.println("Request: " + request.toString());

                ServerLauncher.logger.log(Level.INFO, "Сообщение получено");

                objectInputStream.close();
                byteArrayInputStream.close();
                con.req.clear();
                return request;

            } catch (IOException | ClassNotFoundException e) {
                ServerLauncher.logger.log(Level.SEVERE, "Ошибка во время приема запроса");
                return null;
            }
            catch (NullConParamException e){
                ServerLauncher.logger.log(Level.SEVERE, e.getMessage());
                return null;
            }

    }
}
