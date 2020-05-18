package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class DeleteFileMessage extends Message {

    private static final long serialVersionUID = -7784783834003614281L;
    private FileId fileId;

    public DeleteFileMessage(InetSocketAddress senderAddress, FileId fileId) {
        super(MessageType.DELETE, senderAddress, true);
        this.fileId = fileId;
    }

    public FileId getFileId() {
        return fileId;
    }
}