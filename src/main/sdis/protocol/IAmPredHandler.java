package main.sdis.protocol;

import java.net.InetSocketAddress;

import main.sdis.chord.ChordNode;
import main.sdis.message.Message;

public class IAmPredHandler extends Handler implements Runnable {

    public IAmPredHandler(ChordNode node, Message message) {
        super(node, message);
    }

    @Override
    public void run() {
        InetSocketAddress newPredAddress = message.getHeader().getSenderAddress();

        node.notify(newPredAddress);

        // TODO: reply with OK?
    }
    
}