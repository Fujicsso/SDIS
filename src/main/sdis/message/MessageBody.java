package main.sdis.message;

import java.io.Serializable;
import java.util.Arrays;

public class MessageBody implements Serializable {

    private static final long serialVersionUID = 8848471102293522631L;

    private byte[] content;

    protected MessageBody() {}

    public MessageBody(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return Arrays.toString(content);
    }
}
