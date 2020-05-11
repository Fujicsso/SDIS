package main.sdis.message;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.StringJoiner;

/**
 * Common message header. Any other message headers
 * should inherit from this class
 */
public class MessageHeader implements Serializable {

    private static final long serialVersionUID = 5297031973080582522L;

    protected MessageType messageType;
    protected InetSocketAddress senderAddress;

    protected MessageHeader() {}

    public MessageHeader(MessageType messageType, InetSocketAddress senderAddress) {
        this.messageType = messageType;
        this.senderAddress = senderAddress;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public InetSocketAddress getSenderAddress() {
        return senderAddress;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");

        sj.add(messageType.toString());

        return sj.toString();
    }
}
