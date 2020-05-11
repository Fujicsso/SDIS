package main.sdis.server;

import java.io.ObjectOutputStream;

import main.sdis.common.MessageSender;
import main.sdis.message.ConnectedMessage;
import main.sdis.message.Message;

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
        ConnectedMessage responseMessage = new ConnectedMessage(server.getAddress());

        Connection connection = new Connection(message.getSenderAddress());
        server.addConnection(connection);

        new MessageSender().reply(out, responseMessage);
    }
    
}