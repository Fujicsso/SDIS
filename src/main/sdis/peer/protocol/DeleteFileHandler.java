package main.sdis.peer.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.message.DeleteMessage;
import main.sdis.message.OkMessage;
import main.sdis.peer.PeerImpl;

public class DeleteFileHandler extends Handler<PeerImpl, DeleteMessage> implements Runnable {
    
    public DeleteFileHandler(PeerImpl peer, DeleteMessage message, ObjectOutputStream out) {
        super(peer, message, out);
    }

    @Override
    public void run() {
        node.getStorage().deleteFile(message.getFileId());

        OkMessage responseMessage = new OkMessage(node.getAddress());

        messageSender.reply(out, responseMessage);
    }
}