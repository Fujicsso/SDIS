package main.sdis.protocol;

import main.sdis.chord.ChordNode;
import main.sdis.message.Message;
import main.sdis.message.MessageHeader;
import main.sdis.message.MessageType;

public class GetPredHandler extends Handler implements Runnable {

    public GetPredHandler(ChordNode node, Message message) {
        super(node, message);
    }

    @Override
    public void run() {
        MessageHeader header = message.getHeader();

        MessageHeader responseHeader = new MessageHeader(MessageType.PRED, node.getAddress());
        Message responseMessage = new Message(responseHeader);

        messageSender.sendMessage(responseMessage, header.getSenderAddress().getAddress(), header.getSenderAddress().getPort());

        // TODO: check for OK?
    }
    
}