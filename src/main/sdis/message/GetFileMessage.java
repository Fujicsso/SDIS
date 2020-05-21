package main.sdis.message;

import java.net.InetSocketAddress;

import main.sdis.file.FileId;

public class GetFileMessage extends Message {

    private static final long serialVersionUID = -7784783834003614281L;
    private FileId fileId;

    public GetFileMessage(InetSocketAddress senderAddress, FileId fileId) {
        super(MessageType.GETFILE, senderAddress);
        this.fileId = fileId;
    }

    public FileId getFileId() {
        return fileId;
    }
}