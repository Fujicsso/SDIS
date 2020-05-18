package main.sdis.peer.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.message.DeleteFileMessage;
import main.sdis.message.OkMessage;
import main.sdis.peer.PeerImpl;

public class DeleteFileHandler extends Handler<PeerImpl, DeleteFileMessage> implements Runnable {
    
    public DeleteFileHandler(PeerImpl peer, DeleteFileMessage message, ObjectOutputStream out) {
        super(peer, message, out);
    }

    @Override
    public void run() {
        node.getStorage().deleteFile(message.getFileId());

        OkMessage responseMessage = new OkMessage(node.getAddress());

        messageSender.reply(out, responseMessage);
    }
}