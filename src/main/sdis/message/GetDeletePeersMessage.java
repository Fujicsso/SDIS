package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class GetDeletePeersMessage extends Message {

    private static final long serialVersionUID = 1L;
    public FileId fileId;

    public GetDeletePeersMessage(InetSocketAddress senderAddress, FileId fileId) {
        super(MessageType.GETDELETEPEERS, senderAddress);
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