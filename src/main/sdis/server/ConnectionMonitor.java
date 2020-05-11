package main.sdis.server;

import java.util.Arrays;
import java.util.Iterator;

import main.sdis.common.MessageSender;
import main.sdis.common.Utils;
import main.sdis.message.PingMessage;
import main.sdis.message.PongMessage;

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

            PingMessage message = new PingMessage(server.getAddress());

            PongMessage response = new MessageSender().<PongMessage>sendMessage(message,
                    connection.getClientAddress().getAddress(), connection.getClientAddress().getPort());

            if (response == null)
                itr.remove();
        }
        Utils.safePrintln("Currently connected clients: " + Arrays.toString(server.getConnections().toArray()));
    }
}