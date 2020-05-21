package main.sdis.message;

import java.net.InetSocketAddress;

public class ErrorMessage extends Message {

    private static final long serialVersionUID = 5256346100615482439L;
    private String errorDetails;

    public ErrorMessage(InetSocketAddress senderAddress, String errorDetails) {
        super(MessageType.ERROR, senderAddress);
        this.errorDetails = errorDetails;
    }

    public String getErrorDetails() {
        return errorDetails;
    }
}