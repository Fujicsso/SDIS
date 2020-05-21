package main.sdis.message;

import java.net.InetSocketAddress;

public class ConnectMessage extends Message {

    private static final long serialVersionUID = -3264983785317378349L;

    public ConnectMessage(InetSocketAddress senderAddress) {
        super(MessageType.CONNECT, senderAddress);
    }
}