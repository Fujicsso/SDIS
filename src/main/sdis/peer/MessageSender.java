package main.sdis.peer;

import main.sdis.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MessageSender {

    /**
     * Sends a message to the given address and returns a response message
     * @param message the message to send
     * @param destAddress the destination address
     * @param destPort the destination port number
     * @return a response message
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Message sendMessage(Message message, InetAddress destAddress, int destPort) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(destAddress, destPort);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        out.writeObject(message);

        Message response = (Message) in.readObject();

        socket.close();
        out.close();
        in.close();

        return response;
    }
}
