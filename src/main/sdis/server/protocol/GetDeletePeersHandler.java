package main.sdis.server.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import main.sdis.common.Handler;
import main.sdis.message.DeletePeersMessage;
import main.sdis.message.GetDeletePeersMessage;
import main.sdis.server.Server;

public class GetDeletePeersHandler extends Handler<Server, GetDeletePeersMessage> implements Runnable {

    public GetDeletePeersHandler(Server node, GetDeletePeersMessage message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        List<InetSocketAddress> deletePeers = node.getDeletePeers(message);
        
        DeletePeersMessage response = new DeletePeersMessage(node.getAddress(), deletePeers);
        messageSender.reply(out, response);
    }
    
}