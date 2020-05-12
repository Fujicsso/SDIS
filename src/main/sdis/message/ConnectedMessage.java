package main.sdis.message;

import java.net.InetSocketAddress;

public class ConnectedMessage extends Message {

    private static final long serialVersionUID = -2601522512840838314L;

    public ConnectedMessage(InetSocketAddress senderAddress) {
        super(MessageType.CONNECTED, senderAddress, false);
    }
}