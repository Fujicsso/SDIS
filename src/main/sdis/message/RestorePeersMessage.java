package main.sdis.message;

import java.net.InetSocketAddress;
import java.util.List;

public class RestorePeersMessage extends Message {

    private static final long serialVersionUID = 453336882622670207L;
    List<InetSocketAddress> restorePeers;
    
    public RestorePeersMessage(InetSocketAddress senderAddress, List<InetSocketAddress> restorePeers) {
        super(MessageType.RESTOREPEERS, senderAddress, false);
        this.restorePeers = restorePeers;
    }

    public List<InetSocketAddress> getRestorePeers() {
        return restorePeers;
    }
}