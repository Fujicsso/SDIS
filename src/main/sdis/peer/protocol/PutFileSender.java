package main.sdis.peer.protocol;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

import main.sdis.common.MessageSender;
import main.sdis.message.PutFileMessage;
import main.sdis.message.StoredMessage;

public class PutFileSender implements Callable<StoredMessage> {

    private PutFileMessage message;
    private InetSocketAddress destPeer;

    public PutFileSender(PutFileMessage message, InetSocketAddress destPeer) {
        this.message = message;
        this.destPeer = destPeer;
    }

    @Override
    public StoredMessage call() throws Exception {
        StoredMessage response = new MessageSender().sendMessage(message, destPeer.getAddress(), destPeer.getPort());

        return response;
    }

}