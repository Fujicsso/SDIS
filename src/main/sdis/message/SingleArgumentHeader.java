package main.sdis.message;

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

    public SingleArgumentHeader(String protocolVersion, MessageType messageType, int senderId, Serializable arg) {
        super(protocolVersion, messageType, senderId);
        this.arg = arg;
    }

    public Serializable getArg() {
        return arg;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");

        sj.add(super.toString())
            .add(arg.toString());

        return sj.toString();
    }
    
}