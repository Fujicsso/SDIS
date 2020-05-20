package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class RemovedMessage extends Message {

    private static final long serialVersionUID = -7784783834003614281L;
    private FileId fileId;
    private byte[] fileData;

    public RemovedMessage(InetSocketAddress senderAddress, FileId fileId, byte[] fileData) {
        super(MessageType.REMOVED, senderAddress, true);
        this.fileId = fileId;
        this.fileData = fileData;
    }

    public FileId getFileId() {
        return fileId;
    }

    public byte[] getFileData() {
        return fileData;
    }
}