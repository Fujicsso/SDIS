package main.sdis.server.protocol;

import java.util.concurrent.Callable;

import main.sdis.common.MessageSender;
import main.sdis.message.PutFileMessage;
import main.sdis.message.StoredMessage;
import main.sdis.server.Connection;

public class PutFileSender implements Callable<StoredMessage> {

    private PutFileMessage message;
    private Connection connection;

    public PutFileSender(PutFileMessage message, Connection connection) {
        this.message = message;
        this.connection = connection;
    }

    @Override
    public StoredMessage call() throws Exception {
        StoredMessage response = new MessageSender().sendMessage(message, connection.getClientAddress().getAddress(),
                connection.getClientAddress().getPort());

        return response;
    }

}