package main.sdis.message;

import java.util.List;
import java.net.InetSocketAddress;

public class BackupPeersMessage extends Message {
    
    private static final long serialVersionUID = 2997989804544595569L;
    List<InetSocketAddress> backupPeers;
    
    public BackupPeersMessage(InetSocketAddress senderAddress, List<InetSocketAddress> backupPeers) {
        super(MessageType.BACKUPPEERS, senderAddress, false);
        this.backupPeers = backupPeers;
    }

    public List<InetSocketAddress> getBackupPeers() {
        return backupPeers;
    }
}