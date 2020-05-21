package main.sdis.message;

import java.io.Serializable;
import java.net.InetSocketAddress;

public abstract class Message implements Serializable {

    private static final long serialVersionUID = 4265551115627097390L;

    protected MessageType messageType;
    protected InetSocketAddress senderAddress;

    protected Message() {}

    public Message(MessageType messageType, InetSocketAddress senderAddress) {
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
        return messageType.toString();
    }
}
