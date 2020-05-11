package main.sdis.server;

import java.io.ObjectOutputStream;

import main.sdis.common.MessageSender;
import main.sdis.message.Message;
import main.sdis.message.MessageHeader;
import main.sdis.message.MessageType;

public class ConnectionHandler implements Runnable {
    
    private Server server;
    private Message message;
    private ObjectOutputStream out;

    public ConnectionHandler(Server server, Message message, ObjectOutputStream out) {
        this.server = server;
        this.message = message;
        this.out = out;
    }

    @Override
    public void run() {
        MessageHeader responseHeader = new MessageHeader(MessageType.CONNECTED, server.getAddress());
        Message responseMessage = new Message(responseHeader);

        Connection connection = new Connection(message.getHeader().getSenderAddress());
        server.addConnection(connection);

        new MessageSender().reply(out, responseMessage);
    }
    
}