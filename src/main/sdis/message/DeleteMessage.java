package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class DeleteMessage extends Message {

    private static final long serialVersionUID = -7784783834003614281L;
    private FileId fileId;

    public DeleteMessage(InetSocketAddress senderAddress, FileId fileId) {
        super(MessageType.DELETE, senderAddress);
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