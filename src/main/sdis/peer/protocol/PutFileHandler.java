package main.sdis.peer.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.common.Node;
import main.sdis.message.Message;
import main.sdis.message.StoredMessage;

public class PutFileHandler extends Handler implements Runnable {
    
    public PutFileHandler(Node node, Message message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        StoredMessage responseMessage = new StoredMessage(node.getAddress());

        messageSender.reply(out, responseMessage);
    }
}