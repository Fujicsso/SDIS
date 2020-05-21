package main.sdis.message;

import java.net.InetSocketAddress;
import java.util.List;

public class ReclaimPeersMessage extends Message {

    private static final long serialVersionUID = 4137388499590076335L;
    private List<InetSocketAddress> reclaimPeers;

    public ReclaimPeersMessage(InetSocketAddress senderAddress, List<InetSocketAddress> reclaimPeers) {
        super(MessageType.RECLAIMPEERS, senderAddress);
        this.reclaimPeers = reclaimPeers;
    }

    public List<InetSocketAddress> getReclaimPeers() {
        return reclaimPeers;
    }
}