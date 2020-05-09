package main.sdis.chord;

import main.sdis.message.Message;
import main.sdis.message.MessageHeader;
import main.sdis.message.MessageType;
import main.sdis.peer.MessageSender;

public class PredecessorChecker implements Runnable {

    private ChordNode node;
    private MessageSender messageSender;

    public PredecessorChecker(ChordNode node) {
        this.node = node;
        messageSender = new MessageSender();
    }

    @Override
    public void run() {
        MessageHeader header = new MessageHeader(MessageType.PING, node.getAddress());
        Message message = new Message(header);

        Message responseMessage = messageSender.sendMessage(message, node.getPredecessor().getAddress(),
                node.getPredecessor().getPort());

        if (responseMessage == null || responseMessage.getHeader().getMessageType() != MessageType.PONG)
            node.setPredecessor(null);
    }

}