package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class ConfirmStoredMessage extends Message {

    private static final long serialVersionUID = 61133769429122362L;
    private FileId fileId;
    // Peer who sent the STORED message
    private InetSocketAddress storedPeer;
    private int desiredRepDegree;

    public ConfirmStoredMessage(InetSocketAddress senderAddress, FileId fileId, InetSocketAddress storedPeer,
            int desiredRepDegree) {
        super(MessageType.CONFIRMSTORED, senderAddress, false);
        this.fileId = fileId;
        this.storedPeer = storedPeer;
        this.desiredRepDegree = desiredRepDegree;
    }

    public FileId getFileId() {
        return fileId;
    }

    public InetSocketAddress getStoredPeer() {
        return storedPeer;
    }

    public int getDesiredRepDegree() {
        return desiredRepDegree;
    }
}