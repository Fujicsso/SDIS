package main.sdis.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Node;
import main.sdis.message.Message;
import main.sdis.message.MessageHeader;
import main.sdis.message.MessageType;

public class PingHandler extends Handler implements Runnable {

    public PingHandler(Node node, Message message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        MessageHeader responseHeader = new MessageHeader(MessageType.PONG, node.getAddress());
        Message responseMessage = new Message(responseHeader);

        messageSender.reply(out, responseMessage);
    }
    
}