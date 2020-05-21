package main.sdis.server.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import main.sdis.common.Handler;
import main.sdis.message.BackupPeersMessage;
import main.sdis.message.GetBackupPeersMessage;
import main.sdis.server.Server;

public class GetBackupPeersHandler extends Handler<Server, GetBackupPeersMessage> implements Runnable {

    public GetBackupPeersHandler(Server node, GetBackupPeersMessage message, ObjectOutputStream out) {
        super(node, message, out);
    }

    @Override
    public void run() {
        List<InetSocketAddress> backupPeers = node.getBackupPeers(message);
        
        BackupPeersMessage response = new BackupPeersMessage(node.getAddress(), backupPeers);
        messageSender.reply(out, response);
    }
    
}