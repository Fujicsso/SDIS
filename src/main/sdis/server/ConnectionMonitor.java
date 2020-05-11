package main.sdis.server;

import java.util.Arrays;
import java.util.Iterator;

import main.sdis.common.MessageSender;
import main.sdis.common.Utils;
import main.sdis.message.Message;
import main.sdis.message.MessageHeader;
import main.sdis.message.MessageType;

public class ConnectionMonitor implements Runnable {

    private Server server;

    public ConnectionMonitor(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        Iterator<Connection> itr = server.getConnections().iterator();

        while (itr.hasNext()) {
            // TODO: Ping each client in a separate thread
            Connection connection = itr.next();

            MessageHeader header = new MessageHeader(MessageType.PING, server.getAddress());
            Message message = new Message(header);

            Message response = new MessageSender().sendMessage(message, connection.getClientAddress().getAddress(),
                    connection.getClientAddress().getPort());

            if (response == null)
                itr.remove();
        }
        Utils.safePrintln("Currently connected clients: " + Arrays.toString(server.getConnections().toArray()));
    }
}