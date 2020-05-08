package main.sdis.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;

import main.sdis.chord.ChordNode;
import main.sdis.message.Message;
import main.sdis.message.MessageType;
import main.sdis.message.SingleArgumentHeader;

public class GetPredHandler extends Handler implements Runnable {

    public GetPredHandler(ChordNode node, Message message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        SingleArgumentHeader<InetSocketAddress> responseHeader = new SingleArgumentHeader<InetSocketAddress>(
                MessageType.PRED, node.getAddress(), node.getPredecessor());
        Message responseMessage = new Message(responseHeader);

        messageSender.reply(out, responseMessage);
    }

}