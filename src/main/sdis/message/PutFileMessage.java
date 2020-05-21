package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class PutFileMessage extends Message {

    private static final long serialVersionUID = 4912950447189540153L;
    private FileId fileId;
    private byte[] fileData;
    private int replicationDegree;

    public PutFileMessage(InetSocketAddress senderAddress, FileId fileId, byte[] fileData, int replicationDegree) {
        super(MessageType.PUTFILE, senderAddress);
        this.fileId = fileId;
        this.fileData = fileData;
        this.replicationDegree = replicationDegree;
    }

    public FileId getFileId() {
        return fileId;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public int getReplicationDegree() {
        return replicationDegree;
    }
}