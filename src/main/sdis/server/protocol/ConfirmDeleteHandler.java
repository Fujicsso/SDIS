package main.sdis.server.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.message.ConfirmDeleteMessage;
import main.sdis.message.OkMessage;
import main.sdis.server.Server;

public class ConfirmDeleteHandler extends Handler<Server, ConfirmDeleteMessage> implements Runnable {

    public ConfirmDeleteHandler(Server node, ConfirmDeleteMessage message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        node.getStorage().removeBackedUpFile(message.getFileId(), message.getDeletePeer());

        OkMessage response = new OkMessage(node.getAddress());
        messageSender.reply(out, response);
    }
    
}