package main.sdis.server.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.common.MessageSender;
import main.sdis.message.ConnectMessage;
import main.sdis.message.ConnectedMessage;
import main.sdis.server.Connection;
import main.sdis.server.Server;

public class ConnectionHandler extends Handler<Server, ConnectMessage> implements Runnable {
    
    public ConnectionHandler(Server server, ConnectMessage message, ObjectOutputStream out) {
        super(server, message, out);
    }

    @Override
    public void run() {
        ConnectedMessage responseMessage = new ConnectedMessage(node.getAddress());

        Connection connection = new Connection(message.getSenderAddress());
        node.addConnection(connection);

        new MessageSender().reply(out, responseMessage);
    }
    
}