package main.sdis.server.protocol;

import java.io.ObjectOutputStream;

import main.sdis.common.Handler;
import main.sdis.message.ConfirmStoredMessage;
import main.sdis.message.OkMessage;
import main.sdis.server.Server;

public class ConfirmStoredHandler extends Handler<Server, ConfirmStoredMessage> implements Runnable {

    public ConfirmStoredHandler(Server node, ConfirmStoredMessage message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        node.getStorage().addBackedUpFile(message.getFileId(), message.getDesiredRepDegree(), message.getStoredPeer());

        OkMessage response = new OkMessage(node.getAddress());
        messageSender.reply(out, response);
    }

}