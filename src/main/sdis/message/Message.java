package main.sdis.message;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 4265551115627097390L;

    private MessageHeader header;
    private MessageBody body;

    protected Message() {}

    public Message(MessageHeader header, MessageBody body) {
        this.header = header;
        this.body = body;
    }

    public Message(MessageHeader header) {
        this.header = header;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public MessageBody getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(header.toString());

        if (body != null)
            sb.append(String.format("Body Length = %d\n", body.getContent().length));

        return sb.toString();
    }
}
