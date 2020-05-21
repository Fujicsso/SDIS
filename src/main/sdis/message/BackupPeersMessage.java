package main.sdis.message;

import java.util.Arrays;
import java.util.List;
import java.net.InetSocketAddress;

public class BackupPeersMessage extends Message {
    
    private static final long serialVersionUID = 2997989804544595569L;
    List<InetSocketAddress> backupPeers;
    
    public BackupPeersMessage(InetSocketAddress senderAddress, List<InetSocketAddress> backupPeers) {
        super(MessageType.BACKUPPEERS, senderAddress);
        this.backupPeers = backupPeers;
    }

    public List<InetSocketAddress> getBackupPeers() {
        return backupPeers;
    }

    @Override
    public String toString() {
        return super.toString() + " " + Arrays.toString(backupPeers.toArray());
    }
}