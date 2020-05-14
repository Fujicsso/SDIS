package main.sdis.server.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

import main.sdis.common.Handler;
import main.sdis.message.ErrorMessage;
import main.sdis.message.FileMessage;
import main.sdis.message.GetFileMessage;
import main.sdis.server.Server;

public class GetFileHandler extends Handler<Server, GetFileMessage> implements Runnable {

    public GetFileHandler(Server node, GetFileMessage message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        List<InetSocketAddress> peers = node.getStorage().getPeersOfBackedUpFile(message.getFileId());
        Collections.shuffle(peers);

        for (InetSocketAddress address : peers) {
            FileMessage response = messageSender.<FileMessage>sendMessage(message, address.getAddress(),
                    address.getPort());

            // File has been found among the peers. Send it to the requester peer
            if (response != null) {
                messageSender.reply(out, response);
                return;
            }
        }

        ErrorMessage errorResponse = new ErrorMessage(node.getAddress(), "Could not find file among connected peers");
        messageSender.reply(out, errorResponse);
    }
}