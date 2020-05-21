package main.sdis.message;

import java.net.InetSocketAddress;
import java.util.List;

public class DeletePeersMessage extends Message {

    private static final long serialVersionUID = 1267499691333038214L;
    private List<InetSocketAddress> deletePeers;

    public DeletePeersMessage(InetSocketAddress senderAddress, List<InetSocketAddress> deletePeers) {
        super(MessageType.DELETEPEERS, senderAddress);
        this.deletePeers = deletePeers;
    }

    public List<InetSocketAddress> getDeletePeers() {
        return deletePeers;
    }
}