package main.sdis.message;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * Common message header. Any other message headers
 * should inherit from this class
 */
public class MessageHeader implements Serializable {

    private static final long serialVersionUID = 5297031973080582522L;

    protected String protocolVersion;
    protected MessageType messageType;
    protected int senderId;

    protected MessageHeader() {}

    public MessageHeader(String protocolVersion, MessageType messageType, int senderId) {
        this.protocolVersion = protocolVersion;
        this.messageType = messageType;
        this.senderId = senderId;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public int getSenderId() {
        return senderId;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");

        sj.add(protocolVersion)
            .add(messageType.toString())
            .add(String.valueOf(senderId));

        return sj.toString();
    }
}
