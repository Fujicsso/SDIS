package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class FileMessage extends Message {
    
    private static final long serialVersionUID = -1431166773408249458L;
    private FileId fileId;
    private byte[] fileData;

    public FileMessage(InetSocketAddress senderAddress, FileId fileId, byte[] fileData) {
        super(MessageType.FILE, senderAddress);
        this.fileId = fileId;
        this.fileData = fileData;
    }

    public FileId getFileId() {
        return fileId;
    }

    public byte[] getFileData() {
        return fileData;
    }

    @Override
    public String toString() {
        return super.toString() + " " + fileId;
    }
}