package main.sdis.peer.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.message.PutFileMessage;
import main.sdis.message.StoredMessage;
import main.sdis.peer.PeerImpl;

public class PutFileHandler extends Handler<PeerImpl, PutFileMessage> implements Runnable {
    
    public PutFileHandler(PeerImpl peer, PutFileMessage message, ObjectOutputStream out) {
        super(peer, message, out);
    }

    @Override
    public void run() {
        node.getStorage().saveFile(message.getFileId(), message.getFileData());

        StoredMessage responseMessage = new StoredMessage(node.getAddress());

        messageSender.reply(out, responseMessage);
    }
}