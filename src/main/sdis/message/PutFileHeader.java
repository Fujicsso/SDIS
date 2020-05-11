package main.sdis.message;

import java.net.InetSocketAddress;
import java.util.StringJoiner;

import main.sdis.file.FileId;

/**
 * Represents the header of a PUTFILE message
 */
public class PutFileHeader extends MessageHeader {

    private static final long serialVersionUID = 1737335039972378464L;

    private FileId fileId;
    private int replicationDegree;

    protected PutFileHeader() {}

    public PutFileHeader(MessageType messageType, InetSocketAddress senderAddress, FileId fileId,
            int replicationDegree) {
        super(messageType, senderAddress);
        this.fileId = fileId;
        this.replicationDegree = replicationDegree;
    }

    public FileId getFileId() {
        return fileId;
    }

    public int getReplicationDegree() {
        return replicationDegree;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");

        sj.add(super.toString()).add(fileId.toString()).add(String.valueOf(replicationDegree));

        return sj.toString();
    }
}