package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class GetRestorePeersMessage extends Message {

    private static final long serialVersionUID = -582766099078239317L;
    private FileId fileId;
    
    public GetRestorePeersMessage(InetSocketAddress senderAddress, FileId fileId) {
        super(MessageType.GETRESTOREPEERS, senderAddress);
        this.fileId = fileId;
    }

    public FileId getFileId() {
        return fileId;
    }

    @Override
    public String toString() {
        return super.toString() + " " + fileId;
    }
}