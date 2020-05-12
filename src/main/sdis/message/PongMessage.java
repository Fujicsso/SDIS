package main.sdis.message;

import java.net.InetSocketAddress;

public class PongMessage extends Message {
    
    private static final long serialVersionUID = -3169795959300341989L;

    public PongMessage(InetSocketAddress senderAddress) {
        super(MessageType.PONG, senderAddress, false);
    }
}