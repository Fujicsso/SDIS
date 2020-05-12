package main.sdis.server.protocol;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import main.sdis.common.CustomExecutorService;
import main.sdis.common.MessageSender;
import main.sdis.common.Utils;
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
        // Relay the PUTFILE message to a random set of peers
        // Except to the one that sent it
        List<PutFileSender> putFileTasks = new ArrayList<>();
        List<Connection> randomConnections = new ArrayList<>(server.getConnections());
        randomConnections.removeIf(conn -> conn.getClientAddress().equals(message.getSenderAddress()));
        randomConnections = Utils.randomSubList(randomConnections, message.getReplicationDegree());

        // Avoid backing up a file if it has already been backed up
        // Maybe allow backing up the same file if the replication degree
        // is greater than the current replication degree?
        if (server.hasBackedUpFile(message.getFileId()))
            return;

        for (Connection connection: randomConnections)
            putFileTasks.add(new PutFileSender(message, connection));

        // TODO: repeat backup protocol until replication degree is met
        // Or until max tries reached
        try {
            List<Future<StoredMessage>> futures = executorService.invokeAll(putFileTasks);

            for (Future<StoredMessage> future : futures) {
                StoredMessage storedMessage = future.get();
                
                Utils.safePrintln("STORED MESSAGE: " + storedMessage.toString());

                server.addBackedUpFile(message.getFileId(), storedMessage.getSenderAddress());
            }

            OkMessage response = new OkMessage(server.getAddress());
            messageSender.reply(out, response);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}