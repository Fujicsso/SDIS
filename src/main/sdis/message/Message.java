package main.sdis.message;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.StringJoiner;

public abstract class Message implements Serializable {

    private static final long serialVersionUID = 4265551115627097390L;

    protected MessageType messageType;
    protected InetSocketAddress senderAddress;
    protected boolean isBroadcastable;

    protected Message() {}

    public Message(MessageType messageType, InetSocketAddress senderAddress, boolean isBroadcastable) {
        this.messageType = messageType;
        this.senderAddress = senderAddress;
        this.isBroadcastable = false;
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

        sj.add(messageType.toString())
            .add(senderAddress.toString());

        return sj.toString();
    }
}
