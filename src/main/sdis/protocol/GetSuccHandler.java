package main.sdis.protocol;

import java.net.InetSocketAddress;

import main.sdis.chord.ChordNode;
import main.sdis.chord.Key;
import main.sdis.message.Message;
import main.sdis.message.MessageType;
import main.sdis.message.SingleArgumentHeader;

public class GetSuccHandler extends Handler implements Runnable {

    public GetSuccHandler(ChordNode node, Message message) {
        super(node, message);
    }

    @Override
    public void run() {
        SingleArgumentHeader<Key> header = (SingleArgumentHeader<Key>) message.getHeader();
        InetSocketAddress successor = node.findSuccessor(header.getArg());

        SingleArgumentHeader<InetSocketAddress> responseHeader = new SingleArgumentHeader<InetSocketAddress>(
                MessageType.SUCC, node.getAddress(), successor);

        Message responseMessage = new Message(responseHeader);

        messageSender.sendMessage(responseMessage, header.getSenderAddress().getAddress(),
                header.getSenderAddress().getPort());
        
        // TODO: check for OK?
    }

}