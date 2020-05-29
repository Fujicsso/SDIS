package main.sdis.peer.protocol;

import java.net.InetSocketAddress;
import java.util.List;

import main.sdis.common.MessageSender;
import main.sdis.common.Utils;
import main.sdis.message.Message;
import main.sdis.message.MessageType;
import main.sdis.message.PutFileMessage;
import main.sdis.message.ConfirmStoredMessage;
import main.sdis.message.ErrorMessage;
import main.sdis.peer.PeerImpl;

public class Reclaim implements Runnable {

    private PeerImpl peer;
    private PutFileMessage message;
    private MessageSender messageSender;
    private List<InetSocketAddress> reclaimPeers;

    public Reclaim(PeerImpl peer, PutFileMessage putFileMessage, List<InetSocketAddress> reclaimPeers) {
        this.peer = peer;
        this.message = putFileMessage;
        this.reclaimPeers = reclaimPeers;
        messageSender = new MessageSender();
    }

    @Override
    public void run() {
        InetSocketAddress destPeer = reclaimPeers.get(0);
        Message response = messageSender.sendMessage(message, destPeer.getAddress(), destPeer.getPort());

        if (response == null)
            Utils.safePrintf("An unexpected error occured");
        else if (response.getMessageType() == MessageType.STORED) {
            Utils.safePrintf("Reclaimed %dKB", message.getFileData().length / 1000);

            ConfirmStoredMessage confirmStored = new ConfirmStoredMessage(peer.getAddress(), message.getFileId(),
                    destPeer, message.getReplicationDegree());
            messageSender.sendMessage(confirmStored, peer.getServerAddress().getAddress(), peer.getServerAddress().getPort());
        } else if (response.getMessageType() == MessageType.ERROR)
            Utils.safePrintf(((ErrorMessage) response).getErrorDetails());

    }
}