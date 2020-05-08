package main.sdis.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;

import main.sdis.chord.ChordNode;
import main.sdis.chord.Key;
import main.sdis.message.Message;
import main.sdis.message.MessageType;
import main.sdis.message.SingleArgumentHeader;

public class GetSuccHandler extends Handler implements Runnable {

    public GetSuccHandler(ChordNode node, Message message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        SingleArgumentHeader<Key> header = (SingleArgumentHeader<Key>) message.getHeader();
        InetSocketAddress successor = node.findSuccessor(header.getArg());

        SingleArgumentHeader<InetSocketAddress> responseHeader = new SingleArgumentHeader<InetSocketAddress>(
                MessageType.SUCC, node.getAddress(), successor);

        Message responseMessage = new Message(responseHeader);

        messageSender.reply(out, responseMessage);
    }

}