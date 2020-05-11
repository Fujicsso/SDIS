package main.sdis.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import main.sdis.message.Message;

public class MessageSender {

    /**
     * Sends a message to the given address and returns a response message
     * 
     * @param message     the message to send
     * @param destAddress the destination address
     * @param destPort    the destination port number
     * @return a response message
     */
    public <T extends Message> T sendMessage(Message message, InetAddress destAddress, int destPort) {
        try {
            Socket socket = new Socket(destAddress, destPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(message);

            Utils.safePrintln("SENT MESSAGE [" + message + "] TO " + Utils.formatAddress(destAddress, destPort));

            T response = (T) in.readObject();

            Utils.safePrintln("REPLY FROM " + Utils.formatAddress(response.getSenderAddress()) + " [" + response + "]");

            socket.close();
            out.close();
            in.close();

            return response;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public void reply(ObjectOutputStream out, Message message) {
        try {
            out.writeObject(message);
            Utils.safePrintln("Replied --> " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
