package main.sdis.server.protocol;

import java.io.ObjectOutputStream;
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
import main.sdis.message.ErrorMessage;
import main.sdis.message.OkMessage;
import main.sdis.message.PutFileMessage;
import main.sdis.message.StoredMessage;
import main.sdis.server.Connection;
import main.sdis.server.Server;

public class PutFileHandler implements Runnable {

    private Server server;
    private PutFileMessage message;
    private ObjectOutputStream out;
    private ExecutorService executorService;
    private MessageSender messageSender;

    public PutFileHandler(Server server, PutFileMessage message, ObjectOutputStream out) {
        this.server = server;
        this.message = message;
        this.out = out;
        executorService = CustomExecutorService.getInstance();
        messageSender = new MessageSender();
    }

    @Override
    public void run() {
        final int MAX_ATTEMPTS = 5;
        final int WAIT_TIME = 2;
        int attempts = 0;
        boolean reachedDesiredRepDegree = false;

        // Avoid backing up a file if it has already been backed up
        // The user can trigger a new backup of the file if the desired replication
        // degree
        // Is greater than the current replication degree
        if (server.hasBackedUpFile(message.getFileId())
                && message.getReplicationDegree() <= server.getFileReplicationDegree(message.getFileId())) {
            replyError("File has already been backed up with desired replication degree");
            return;
        }

        // Relay the PUTFILE message to a random set of peers
        // except to the one that sent it and peers who have already
        // backed up the file
        while (!reachedDesiredRepDegree && attempts < MAX_ATTEMPTS) {
            try {
                TimeUnit.SECONDS.sleep(WAIT_TIME);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            List<PutFileSender> putFileTasks = new ArrayList<>();
            List<InetSocketAddress> peersOfFile = server.getPeersOfBackedUpFile(message.getFileId());

            List<Connection> randomConnections = new ArrayList<>(server.getConnections());
            randomConnections.removeIf(conn -> conn.getClientAddress().equals(message.getSenderAddress())
                    || (peersOfFile != null && peersOfFile.contains(conn.getClientAddress())));
            randomConnections = Utils.randomSubList(randomConnections, message.getReplicationDegree());

            for (Connection connection : randomConnections)
                putFileTasks.add(new PutFileSender(message, connection));

            try {
                List<Future<StoredMessage>> futures = executorService.invokeAll(putFileTasks);

                for (Future<StoredMessage> future : futures) {
                    StoredMessage storedMessage = future.get();

                    Utils.safePrintln("STORED MESSAGE: " + storedMessage.toString());

                    server.addBackedUpFile(message.getFileId(), storedMessage.getSenderAddress());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // Check if desired replication degree has been reached
            if (server.getFileReplicationDegree(message.getFileId()) >= message.getReplicationDegree())
                reachedDesiredRepDegree = true;
            else
                Utils.safePrintln("Could not reach desired replication degree. Retrying...");

            attempts++;
        }

        if (reachedDesiredRepDegree) {
            replyOk();
        } else {
            replyError("Could not backup file with desired replication degree. Current replication degree is "
                    + server.getFileReplicationDegree(message.getFileId()));
        }
    }

    private void replyOk() {
        OkMessage response = new OkMessage(server.getAddress());
        messageSender.reply(out, response);
    }

    private void replyError(String errorMessage) {
        ErrorMessage response = new ErrorMessage(server.getAddress(), errorMessage);
        messageSender.reply(out, response);
    }
}