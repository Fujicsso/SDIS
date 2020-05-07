package main.sdis.protocol;

import java.net.InetSocketAddress;

import main.sdis.chord.ChordNode;
import main.sdis.message.Message;
import main.sdis.message.SingleArgumentHeader;

public class IAmPredHandler extends Handler implements Runnable {

    public IAmPredHandler(ChordNode node, Message message) {
        super(node, message);
    }

    @Override
    public void run() {
        SingleArgumentHeader<InetSocketAddress> header = (SingleArgumentHeader<InetSocketAddress>) message.getHeader();

        InetSocketAddress newPredAddress = header.getArg();

        node.notify(newPredAddress);
    }
    
}