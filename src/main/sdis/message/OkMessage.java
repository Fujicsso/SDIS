package main.sdis.message;

import java.net.InetSocketAddress;

public class OkMessage extends Message {

    private static final long serialVersionUID = 8276216865278169058L;

    public OkMessage(InetSocketAddress senderAddress) {
        super(MessageType.OK, senderAddress, false);
    }
}