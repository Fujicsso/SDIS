package main.sdis.message;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class RestorePeersMessage extends Message {

    private static final long serialVersionUID = 453336882622670207L;
    List<InetSocketAddress> restorePeers;
    
    public RestorePeersMessage(InetSocketAddress senderAddress, List<InetSocketAddress> restorePeers) {
        super(MessageType.RESTOREPEERS, senderAddress);
        this.restorePeers = restorePeers;
    }

    public List<InetSocketAddress> getRestorePeers() {
        return restorePeers;
    }

    @Override
    public String toString() {
        return super.toString() + " " + Arrays.toString(restorePeers.toArray());
    }
}