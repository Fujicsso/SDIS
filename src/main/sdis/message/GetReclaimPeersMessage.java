package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class GetReclaimPeersMessage extends Message {
    
    private static final long serialVersionUID = 7547334190069012333L;

    private FileId fileId;

    public GetReclaimPeersMessage(InetSocketAddress senderAddress, FileId fileId) {
        super(MessageType.GETRECLAIMPEERS, senderAddress);
        this.fileId = fileId;
    }

    public FileId getFileId(){
        return this.fileId;
    }
}