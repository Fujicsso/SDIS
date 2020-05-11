package main.sdis.message;

import java.net.InetSocketAddress;

public class ConnectMessage extends Message {

    private static final long serialVersionUID = 1L;

    public ConnectMessage(InetSocketAddress senderAddress) {
        super(MessageType.CONNECT, senderAddress, false);
    }
}