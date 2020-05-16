package main.sdis.server;

import main.sdis.common.CustomExecutorService;
import main.sdis.message.PingMessage;
import main.sdis.server.protocol.PingSender;

public class ConnectionMonitor implements Runnable {

    private Server server;

    public ConnectionMonitor(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        for (Connection connection : server.getConnections()) {
            PingMessage message = new PingMessage(server.getAddress());

            CustomExecutorService.getInstance().execute(new PingSender(server, message, connection));
        }
    }
}