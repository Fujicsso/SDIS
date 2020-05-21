package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class GetBackupPeersMessage extends Message {

    private static final long serialVersionUID = 2015533431002327123L;
    private FileId fileId;
    private int replicationDegree;
    
    public GetBackupPeersMessage(InetSocketAddress senderAddress, FileId fileId, int replicationDegree) {
        super(MessageType.GETBACKUPPEERS, senderAddress, false);
        this.fileId = fileId;
        this.replicationDegree = replicationDegree;
    }

    public FileId getFileId() {
        return fileId;
    }

    public int getReplicationDegree() {
        return replicationDegree;
    }
}