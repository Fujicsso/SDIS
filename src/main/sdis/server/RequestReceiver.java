package main.sdis.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import main.sdis.common.CustomExecutorService;
import main.sdis.common.Utils;
import main.sdis.message.ConnectMessage;
import main.sdis.message.DeleteFileMessage;
import main.sdis.message.GetFileMessage;
import main.sdis.message.Message;
import main.sdis.message.PutFileMessage;
import main.sdis.message.RemovedMessage;
import main.sdis.server.protocol.ConnectionHandler;
import main.sdis.server.protocol.GetFileHandler;
import main.sdis.server.protocol.PutFileHandler;
import main.sdis.server.protocol.DeleteFileHandler;
import main.sdis.server.protocol.RemovedHandler;

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
                    case PUTFILE:
                        executorService.execute(new PutFileHandler(server, (PutFileMessage) message, out));
                        break;
                    case GETFILE:
                        executorService.execute(new GetFileHandler(server, (GetFileMessage) message, out));
                        break;
                    case DELETE:
                        executorService.execute(new DeleteFileHandler(server, (DeleteFileMessage) message, out));
                        break;
                    case REMOVED:
                        executorService.execute(new RemovedHandler(server, (RemovedMessage) message, out));
                        break;
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}