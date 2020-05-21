package main.sdis.message;

import java.net.InetSocketAddress;

public class GetReclaimPeersMessage extends Message {
    
    private static final long serialVersionUID = 7547334190069012333L;

    public GetReclaimPeersMessage(InetSocketAddress senderAddress) {
        super(MessageType.GETRECLAIMPEERS, senderAddress);
    }
}