package main.sdis.peer.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.message.FileMessage;
import main.sdis.message.GetFileMessage;
import main.sdis.peer.PeerImpl;

public class GetFileHandler extends Handler<PeerImpl, GetFileMessage> implements Runnable {

    public GetFileHandler(PeerImpl node, GetFileMessage message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        byte[] fileData = node.getStorage().getFileData(message.getFileId());

        FileMessage response = new FileMessage(node.getAddress(), message.getFileId(), fileData);

        messageSender.reply(out, response);
    }
    
}