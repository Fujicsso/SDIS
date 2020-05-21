package main.sdis.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import main.sdis.common.CustomExecutorService;
import main.sdis.common.Utils;
import main.sdis.message.ConfirmDeleteMessage;
import main.sdis.message.ConfirmStoredMessage;
import main.sdis.message.ConnectMessage;
import main.sdis.message.GetBackupPeersMessage;
import main.sdis.message.GetDeletePeersMessage;
import main.sdis.message.GetReclaimPeersMessage;
import main.sdis.message.GetRestorePeersMessage;
import main.sdis.message.Message;
import main.sdis.server.protocol.ConfirmDeleteHandler;
import main.sdis.server.protocol.ConfirmStoredHandler;
import main.sdis.server.protocol.ConnectionHandler;
import main.sdis.server.protocol.GetReclaimPeersHandler;
import main.sdis.server.protocol.GetRestorePeersHandler;
import main.sdis.server.protocol.GetBackupPeersHandler;
import main.sdis.server.protocol.GetDeletePeersHandler;

public class RequestReceiver implements Runnable {

    private Server server;
    private ServerSocket socket;
    private ExecutorService executorService;

    public RequestReceiver(Server server) throws IOException {
        this.server = server;
        this.socket = new ServerSocket(server.getAddress().getPort());
        executorService = CustomExecutorService.getInstance();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = socket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                Message message = (Message) in.readObject();

                Utils.safePrintln("MessageReceiver: RECEIVED MESSAGE FROM "
                        + Utils.formatAddress(message.getSenderAddress()) + " [" + message + "]");

                switch (message.getMessageType()) {
                    case CONNECT:
                        executorService.execute(new ConnectionHandler(server, (ConnectMessage) message, out));
                        break;
                    case GETBACKUPPEERS:
                        executorService.execute(new GetBackupPeersHandler(server, (GetBackupPeersMessage) message, out));
                        break;
                    case GETDELETEPEERS:
                        executorService.execute(new GetDeletePeersHandler(server, (GetDeletePeersMessage) message, out));
                        break;
                    case GETRESTOREPEERS:
                        executorService.execute(new GetRestorePeersHandler(server, (GetRestorePeersMessage) message, out));
                        break;
                    case GETRECLAIMPEERS:
                        executorService.execute(new GetReclaimPeersHandler(server, (GetReclaimPeersMessage) message, out));
                        break;
                    case CONFIRMSTORED:
                        executorService.execute(new ConfirmStoredHandler(server, (ConfirmStoredMessage) message, out));
                        break;
                    case CONFIRMDELETE:
                        executorService.execute(new ConfirmDeleteHandler(server, (ConfirmDeleteMessage) message, out));
                        break;
                    default:
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}