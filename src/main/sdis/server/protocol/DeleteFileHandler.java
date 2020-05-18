package main.sdis.server.protocol;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import main.sdis.common.Utils;
import main.sdis.server.Connection;
import main.sdis.server.Server;
import main.sdis.message.DeleteFileMessage;
import main.sdis.message.OkMessage;
import main.sdis.common.CustomExecutorService;
import main.sdis.common.MessageSender;

public class DeleteFileHandler implements Runnable {

    private Server server;
    private DeleteFileMessage message;
    private ObjectOutputStream out;
    private ExecutorService executorService;
    private MessageSender messageSender;

    public DeleteFileHandler(Server server, DeleteFileMessage message, ObjectOutputStream out) {
        this.server = server;
        this.message = message;
        this.out = out;
        executorService = CustomExecutorService.getInstance();
        messageSender = new MessageSender();
    }

    @Override
    public void run() {
        do {
            List<InetSocketAddress> peersWithFile = server.getStorage().getPeersOfBackedUpFile(message.getFileId());
            List<Connection> randomConnections = new ArrayList<>(server.getConnections());

            randomConnections.removeIf(conn -> conn.getClientAddress().equals(message.getSenderAddress())
                    || (peersWithFile != null && !peersWithFile.contains(conn.getClientAddress())));

            for (Connection connection : randomConnections) {
                try {
                    Future<OkMessage> future = executorService.submit(new DeleteFileSender(message, connection));
                    OkMessage okMessage = future.get();
                    Utils.safePrintln("OK MESSAGE: " + okMessage.toString());
                    server.getStorage().removeBackedUpFile(message.getFileId(), okMessage.getSenderAddress());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            }

        } while (server.getStorage().getFileReplicationDegree(message.getFileId()) != 0);
        OkMessage response = new OkMessage(server.getAddress());
        messageSender.reply(out, response);
    }
}