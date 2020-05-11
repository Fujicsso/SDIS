package main.sdis.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;

import main.sdis.chord.ChordNode;
import main.sdis.message.Message;
import main.sdis.message.MessageHeader;
import main.sdis.message.MessageType;

public class IAmPredHandler extends Handler implements Runnable {

    public IAmPredHandler(ChordNode node, Message message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        InetSocketAddress newPredAddress = message.getHeader().getSenderAddress();

        node.notify(newPredAddress);

        MessageHeader responseHeader = new MessageHeader(MessageType.OK, node.getAddress());
        Message responseMessage = new Message(responseHeader);

        messageSender.reply(out, responseMessage);
    }
    
}