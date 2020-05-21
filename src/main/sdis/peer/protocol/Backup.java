package main.sdis.peer.protocol;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import main.sdis.common.CustomExecutorService;
import main.sdis.common.MessageSender;
import main.sdis.common.Utils;
import main.sdis.message.BackupPeersMessage;
import main.sdis.message.ConfirmStoredMessage;
import main.sdis.message.GetBackupPeersMessage;
import main.sdis.message.PutFileMessage;
import main.sdis.message.StoredMessage;
import main.sdis.peer.PeerImpl;

public class Backup implements Runnable {

    private PeerImpl peer;
    private PutFileMessage message;
    private ExecutorService executorService;
    private MessageSender messageSender;

    public Backup(PeerImpl peer, PutFileMessage message) {
        this.peer = peer;
        this.message = message;
        executorService = CustomExecutorService.getInstance();
        messageSender = new MessageSender();
    }

    @Override
    public void run() {
        final int MAX_ATTEMPTS = 5;
        final int WAIT_TIME = 2;
        int attempts = 0;
        boolean reachedDesiredRepDegree = false;
        int currRepDegree = 0;

        // Relay the PUTFILE message to a random set of peers
        // except to the one that sent it and peers who have already
        // backed up the file
        while (!reachedDesiredRepDegree && attempts < MAX_ATTEMPTS) {
            try {
                TimeUnit.SECONDS.sleep(WAIT_TIME);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            // update list peers
            GetBackupPeersMessage getBackupPeersMessage = new GetBackupPeersMessage(peer.getAddress(),
                    message.getFileId(), message.getReplicationDegree());

            BackupPeersMessage getBackupPeersResponse = messageSender.sendMessage(getBackupPeersMessage,
                    peer.getServerAddress().getAddress(), peer.getServerAddress().getPort());

            List<InetSocketAddress> backupPeers = getBackupPeersResponse.getBackupPeers();

            List<PutFileSender> putFileTasks = new ArrayList<>();

            for (InetSocketAddress peerAddress : backupPeers)
                putFileTasks.add(new PutFileSender(message, peerAddress));

            try {
                List<Future<StoredMessage>> futures = executorService.invokeAll(putFileTasks);

                for (Future<StoredMessage> future : futures) {
                    StoredMessage storedMessage = future.get();

                    // If a STORED message is received, we can be sure the file was backed up
                    currRepDegree++;
                    // Tell the server a STORED message was received
                    ConfirmStoredMessage confirmStoredMesssage = new ConfirmStoredMessage(peer.getAddress(),
                            message.getFileId(), storedMessage.getSenderAddress(), message.getReplicationDegree());

                    messageSender.sendMessage(confirmStoredMesssage, peer.getServerAddress().getAddress(),
                            peer.getServerAddress().getPort());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // Check if desired replication degree has been reached
            if (currRepDegree >= message.getReplicationDegree())
                reachedDesiredRepDegree = true;
            else {
                Utils.safePrintln("Could not reach desired replication degree. Retrying...");
                try {
                    TimeUnit.SECONDS.sleep(WAIT_TIME);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            attempts++;
        }

        if (reachedDesiredRepDegree) {
            Utils.safePrintln("File backed up successfully");
        } else {
            Utils.safePrintln("Could not backup file with desired replication degree. Current replication degree is "
                    + currRepDegree);
        }
    }
}