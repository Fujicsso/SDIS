package main.sdis.message;

import java.net.InetSocketAddress;

public class StoredMessage extends Message {

    private static final long serialVersionUID = -3971976637643287468L;

    public StoredMessage(InetSocketAddress senderAddress) {
        super(MessageType.STORED, senderAddress, true);
    }
    
}