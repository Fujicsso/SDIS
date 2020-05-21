package main.sdis.peer.protocol;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import main.sdis.common.Utils;
import main.sdis.server.Connection;
import main.sdis.message.ConfirmDeleteMessage;
import main.sdis.message.DeleteMessage;
import main.sdis.message.DeletePeersMessage;
import main.sdis.message.GetDeletePeersMessage;
import main.sdis.message.OkMessage;
import main.sdis.peer.PeerImpl;
import main.sdis.common.CustomExecutorService;
import main.sdis.common.MessageSender;

public class Delete implements Runnable {

    private PeerImpl peer;
    private DeleteMessage message;
    private ExecutorService executorService;
    private MessageSender messageSender;

    public Delete(PeerImpl peer, DeleteMessage message) {
        this.peer = peer;
        this.message = message;
        executorService = CustomExecutorService.getInstance();
        messageSender = new MessageSender();
    }

    @Override
    public void run() {
        boolean allFilesDeleted = false;
        final int MAX_ATTEMPTS = 5;
        final int WAIT_TIME = 5;
        int attempts = 0;

        while (!allFilesDeleted && attempts < MAX_ATTEMPTS) {
            List<DeleteFileSender> deleteTasks = new ArrayList<>();

            GetDeletePeersMessage getDeletePeersMessage = new GetDeletePeersMessage(peer.getAddress(), message.getFileId());
            DeletePeersMessage getDeletePeersResponse = messageSender.sendMessage(getDeletePeersMessage,
            peer.getServerAddress().getAddress(), peer.getServerAddress().getPort());

            List<InetSocketAddress> deletePeers = getDeletePeersResponse.getDeletePeers();

            // Return immediately if there aren't any peers with the file
            if (deletePeers.isEmpty()) {
                allFilesDeleted = true;
                break;
            }

            for (InetSocketAddress address : deletePeers) {
                Connection connection = new Connection(address);
                deleteTasks.add(new DeleteFileSender(message, connection));
            }

            try {
                allFilesDeleted = true;
                List<Future<OkMessage>> futures = executorService.invokeAll(deleteTasks);

                for (Future<OkMessage> future : futures) {
                    OkMessage okMessage = future.get();

                    if (okMessage == null)
                        allFilesDeleted = false;
                    else {
                        ConfirmDeleteMessage confirmDeleteMessage = new ConfirmDeleteMessage(peer.getAddress(),
                                message.getFileId(), okMessage.getSenderAddress());
    
                        messageSender.sendMessage(confirmDeleteMessage, peer.getServerAddress().getAddress(),
                                peer.getServerAddress().getPort());
                    }
                }
            } catch (InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }

            if (!allFilesDeleted) {
                Utils.safePrintln("Could not delete all copies of the file. Retrying...");
                try {
                    TimeUnit.SECONDS.sleep(WAIT_TIME);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            attempts++;
        }
        if (allFilesDeleted)
            Utils.safePrintln("Successfully deleted all copies of the file");
        else
            Utils.safePrintln("Could not delete all copies of the file");
    }
}