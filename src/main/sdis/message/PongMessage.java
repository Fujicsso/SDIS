package main.sdis.message;

import java.net.InetSocketAddress;

public class PongMessage extends Message {
    
    private static final long serialVersionUID = 1L;

    public PongMessage(InetSocketAddress senderAddress) {
        super(MessageType.PONG, senderAddress, false);
    }
}