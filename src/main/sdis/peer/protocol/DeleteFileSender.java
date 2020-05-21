package main.sdis.peer.protocol;

import java.util.concurrent.Callable;

import main.sdis.common.MessageSender;
import main.sdis.message.DeleteMessage;
import main.sdis.message.OkMessage;
import main.sdis.server.Connection;

public class DeleteFileSender implements Callable<OkMessage> {

    private DeleteMessage message;
    private Connection connection;

    public DeleteFileSender(DeleteMessage message, Connection connection) {
        this.message = message;
        this.connection = connection;
    }

    @Override
    public OkMessage call() throws Exception {
        OkMessage response = new MessageSender().sendMessage(message, connection.getClientAddress().getAddress(),
                connection.getClientAddress().getPort());

        return response;
    }

}