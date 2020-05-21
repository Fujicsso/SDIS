package main.sdis.server.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import main.sdis.common.Handler;
import main.sdis.message.GetReclaimPeersMessage;
import main.sdis.message.ReclaimPeersMessage;
import main.sdis.server.Server;

public class GetReclaimPeersHandler extends Handler<Server, GetReclaimPeersMessage> implements Runnable {

    public GetReclaimPeersHandler(Server node, GetReclaimPeersMessage message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        List<InetSocketAddress> reclaimPeers = node.getReclaimPeers();
        
        ReclaimPeersMessage response = new ReclaimPeersMessage(node.getAddress(), reclaimPeers);
        messageSender.reply(out, response);
    }
    
}