package main.sdis.message;

import java.net.InetSocketAddress;

public class PingMessage extends Message {
    
    private static final long serialVersionUID = -4478599616804940184L;

    public PingMessage(InetSocketAddress senderAddress) {
        super(MessageType.PING, senderAddress, false);
    }
}