package main.sdis.peer.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.common.Node;
import main.sdis.message.Message;
import main.sdis.message.PongMessage;

public class PingHandler extends Handler implements Runnable {

    public PingHandler(Node node, Message message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        PongMessage responseMessage = new PongMessage(node.getAddress());

        messageSender.reply(out, responseMessage);
    }
    
}