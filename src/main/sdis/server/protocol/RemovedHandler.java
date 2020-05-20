package main.sdis.server.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import main.sdis.common.CustomExecutorService;
import main.sdis.common.Handler;
import main.sdis.message.OkMessage;
import main.sdis.message.PutFileMessage;
import main.sdis.message.RemovedMessage;
import main.sdis.server.Server;
import main.sdis.server.Storage;

public class RemovedHandler extends Handler<Server, RemovedMessage> implements Runnable {


    public RemovedHandler(Server node, RemovedMessage message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        List<InetSocketAddress> blackList = new ArrayList<>();
        blackList.add(message.getSenderAddress());
        
        Storage storage = node.getStorage();
        storage.removeBackedUpFile(message.getFileId(), message.getSenderAddress());

        int currRepDegree = storage.getFileReplicationDegree(message.getFileId());
        int desRepDegree = storage.getDesiredRepDegree(message.getFileId());

        if (currRepDegree < desRepDegree) {
            PutFileMessage putFileMessage = new PutFileMessage(node.getAddress(), message.getFileId(),
                    message.getFileData(), desRepDegree);
            CustomExecutorService.getInstance().execute(new PutFileHandler(node, putFileMessage, out, blackList));        
        }

        OkMessage response = new OkMessage(node.getAddress());
        messageSender.reply(out, response);
    }

}