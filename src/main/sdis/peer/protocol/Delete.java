package main.sdis.peer.protocol;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import main.sdis.common.Utils;
import main.sdis.server.Connection;
import main.sdis.message.ConfirmDeleteMessage;
import main.sdis.message.DeleteMessage;
import main.sdis.message.OkMessage;
import main.sdis.peer.PeerImpl;
import main.sdis.common.CustomExecutorService;
import main.sdis.common.MessageSender;

public class Delete implements Runnable {

    private PeerImpl peer;
    private DeleteMessage message;
    private List<InetSocketAddress> deletePeers;
    private ExecutorService executorService;
    private MessageSender messageSender;

    public Delete(PeerImpl peer, DeleteMessage message, List<InetSocketAddress> deletePeers) {
        this.peer = peer;
        this.message = message;
        this.deletePeers = deletePeers;
        executorService = CustomExecutorService.getInstance();
        messageSender = new MessageSender();
    }

    @Override
    public void run() {
            List<DeleteFileSender> deleteFileTasks = new ArrayList<>();
            int countFailures = 0;

            for (InetSocketAddress address : deletePeers) {
                Connection connection = new Connection(address);
                deleteFileTasks.add(new DeleteFileSender(message, connection));
            }

            try {
                List<Future<OkMessage>> futures = executorService.invokeAll(deleteFileTasks);
                for (Future<OkMessage> future : futures) {
                    OkMessage okMessage = future.get();

                    if (okMessage == null)
                        countFailures++;
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

        if (countFailures == 0)
            Utils.safePrintln("Successfully deleted all copies of the file");
        else
            Utils.safePrintln("Could not delete all copies of the file");
    }
}