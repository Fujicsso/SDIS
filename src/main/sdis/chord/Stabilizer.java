package main.sdis.chord;

import java.net.InetSocketAddress;

import main.sdis.common.Utils;
import main.sdis.message.Message;
import main.sdis.message.MessageHeader;
import main.sdis.message.MessageType;
import main.sdis.message.SingleArgumentHeader;
import main.sdis.peer.MessageSender;

public class Stabilizer implements Runnable {

    private ChordNode node;
    private MessageSender messageSender;

    public Stabilizer(ChordNode node) {
        this.node = node;
        this.messageSender = new MessageSender();
    }

    @Override
    public void run() {
        InetSocketAddress succAddress = node.getSuccessorAddress();

        MessageHeader header = new MessageHeader(MessageType.GETPRED, node.getAddress());
        Message message = new Message(header);

        Message response = messageSender.sendMessage(message, succAddress.getAddress(), succAddress.getPort());
        SingleArgumentHeader<InetSocketAddress> responseHeader = (SingleArgumentHeader<InetSocketAddress>) response.getHeader();

        InetSocketAddress succPredAddress = responseHeader.getArg();
        Key predKey = new NodeKey(succPredAddress);

        if (Utils.isKeyInOpenInterval(predKey, node.getKey(), node.getSuccessorKey())) {
            header = new MessageHeader(MessageType.IAMPRED, node.getAddress());
            message = new Message(header);

            messageSender.sendMessage(message, succPredAddress.getAddress(), succPredAddress.getPort());
            // TODO: check for OK?
        }
    }
    
}