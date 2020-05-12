package main.sdis.peer;

import main.sdis.common.CustomExecutorService;
import main.sdis.common.Utils;
import main.sdis.message.Message;
import main.sdis.peer.protocol.PingHandler;
import main.sdis.peer.protocol.PutFileHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class MessageReceiver implements Runnable {

    private PeerImpl peer;
    private ServerSocket socket;
    private ExecutorService executorService;

    public MessageReceiver(PeerImpl peer) throws IOException {
        this.peer = peer;
        this.socket = new ServerSocket(this.peer.getAddress().getPort());
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
                    case PING:
                        executorService.execute(new PingHandler(peer, message, out));
                        break;
                    case PUTFILE:
                        executorService.execute(new PutFileHandler(peer, message, out));
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
