package main.sdis.peer;

import main.sdis.message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MessageSender {

    public void sendMessage(Message message, InetAddress destAddress, int destPort) {
        try {
            Socket socket = new Socket(destAddress, destPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);
            socket.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
