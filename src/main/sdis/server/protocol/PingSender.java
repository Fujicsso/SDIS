package main.sdis.server.protocol;

import java.util.Arrays;

import main.sdis.common.MessageSender;
import main.sdis.common.Utils;
import main.sdis.message.PingMessage;
import main.sdis.message.PongMessage;
import main.sdis.server.Connection;
import main.sdis.server.Server;

public class PingSender implements Runnable {

    private Server server;
    private PingMessage message;
    private Connection connection;

    public PingSender(Server server, PingMessage message, Connection connection) {
        this.server = server;
        this.message = message;
        this.connection = connection;
    }

	@Override
	public void run() {
        PongMessage response = new MessageSender().<PongMessage>sendMessage(message,
                    connection.getClientAddress().getAddress(), connection.getClientAddress().getPort());

        if (response == null)
            server.removeConnection(connection);

        Utils.safePrintln("Currently connected clients: " + Arrays.toString(server.getConnections().toArray()));
	}
}