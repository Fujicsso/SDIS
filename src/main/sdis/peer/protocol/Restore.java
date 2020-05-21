package main.sdis.peer.protocol;

import java.net.InetSocketAddress;
import java.util.List;

import main.sdis.common.MessageSender;
import main.sdis.common.Utils;
import main.sdis.message.FileMessage;
import main.sdis.message.GetFileMessage;
import main.sdis.peer.PeerImpl;

public class Restore implements Runnable {

    private PeerImpl peer;
    private GetFileMessage message;
    private MessageSender messageSender;
    private List<InetSocketAddress> restorePeers;

    public Restore(PeerImpl peer, GetFileMessage message, List<InetSocketAddress> restorePeers) {
        this.peer = peer;
        this.message = message;
        this.restorePeers = restorePeers;
        messageSender = new MessageSender();
    }

    @Override
    public void run() {
        for (InetSocketAddress address : restorePeers) {
            FileMessage response = messageSender.<FileMessage>sendMessage(message, address.getAddress(),
                    address.getPort());

            // File has been found among the peers. Send it to the requester peer
            if (response != null) {
                peer.getStorage().restoreFile(response.getFileId(), response.getFileData());
                return;
            } else {
                Utils.safePrintln("Could not find file among connected peers");
            }
        }
    }
}