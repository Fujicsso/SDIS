package main.sdis.peer.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.message.PingMessage;
import main.sdis.message.PongMessage;
import main.sdis.peer.PeerImpl;

public class PingHandler extends Handler<PeerImpl, PingMessage> implements Runnable {

    public PingHandler(PeerImpl peer, PingMessage message, ObjectOutputStream out) {
        super(peer, message, out);
    }

    @Override
    public void run() {
        PongMessage responseMessage = new PongMessage(node.getAddress());

        messageSender.reply(out, responseMessage);
    }
    
}