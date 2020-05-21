package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class ConfirmDeleteMessage extends Message {
    
    private static final long serialVersionUID = 6264022130576855486L;
    private FileId fileId;
    // Peer who sent the DELETE message
    private InetSocketAddress deletePeer;

    public ConfirmDeleteMessage(InetSocketAddress senderAddress, FileId fileId, InetSocketAddress deletePeer) {
        super(MessageType.CONFIRMDELETE, senderAddress, false);
        this.fileId = fileId;
        this.deletePeer = deletePeer;
    }

    public FileId getFileId() {
        return fileId;
    }

    public InetSocketAddress getDeletePeer() {
        return deletePeer;
    }
}