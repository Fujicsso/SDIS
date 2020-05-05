package main.sdis.message;

import java.util.StringJoiner;

import main.sdis.file.FileId;

/**
 * Represents the header of a PUTFILE message
 */
public class PutFileHeader extends MessageHeader {

    private static final long serialVersionUID = 1737335039972378464L;

    private FileId fileId;
    private int replicationDegree;

    public PutFileHeader(String protocolVersion, MessageType messageType, int senderId, FileId fileId, int replicationDegree) {
        super(protocolVersion, messageType, senderId);
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

        sj.add(super.toString())
            .add(fileId.toString())
            .add(String.valueOf(replicationDegree));

        return sj.toString();
    }
}