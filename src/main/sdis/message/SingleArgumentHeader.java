package main.sdis.message;

import java.net.InetSocketAddress;
import java.util.StringJoiner;

/**
 * A message header which can take an aditional argument Used for the following
 * message types: STORED GETFILE FILE REMOVED DELETE GETSUCC SUCC GETPRED PRED
 * 
 * @param <Serializable> any serializable type
 */
public class SingleArgumentHeader<Serializable> extends MessageHeader {

    private static final long serialVersionUID = -1031205545252007158L;
    private Serializable arg;

    protected SingleArgumentHeader() {}

    public SingleArgumentHeader(MessageType messageType, InetSocketAddress senderAddress,
            Serializable arg) {
        super(messageType, senderAddress);
        this.arg = arg;
    }

    public Serializable getArg() {
        return arg;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");

        sj.add(super.toString()).add(arg.toString());

        return sj.toString();
    }

}