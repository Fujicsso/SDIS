package main.sdis.server.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import main.sdis.common.Handler;
import main.sdis.message.GetRestorePeersMessage;
import main.sdis.message.RestorePeersMessage;
import main.sdis.server.Server;

public class GetRestorePeersHandler extends Handler<Server, GetRestorePeersMessage> implements Runnable {

    public GetRestorePeersHandler(Server node, GetRestorePeersMessage message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        List<InetSocketAddress> restorePeers = node.getRestorePeers(message);
        
        RestorePeersMessage response = new RestorePeersMessage(node.getAddress(), restorePeers);
        messageSender.reply(out, response);
    }
    
}