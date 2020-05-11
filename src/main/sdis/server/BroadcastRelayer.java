package main.sdis.server;

import main.sdis.common.MessageSender;
import main.sdis.message.Message;

public class BroadcastRelayer {

    private Server server;
    private MessageSender messageSender;

    public BroadcastRelayer(Server server) {
        this.server = server;
        messageSender = new MessageSender();
    }

    public void relayMessage(Message message) {
        for (Connection connection : server.getConnections()) {
            // TODO: send each message on a separate thread
            Message response = messageSender.sendMessage(message, connection.getClientAddress().getAddress(),
                    connection.getClientAddress().getPort());

            // Pass handling of the response to a handler
            // For example, if the server sends a PUTFILE message to a peer
            // It is expected that the peer will reply with a STORED message (if it stored the file)
            switch (response.getMessageType()) {
                
            }
        }
    }
}