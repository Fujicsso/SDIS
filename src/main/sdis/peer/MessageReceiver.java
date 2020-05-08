package main.sdis.peer;

import main.sdis.chord.ChordNode;
import main.sdis.common.CustomExecutorService;
import main.sdis.common.Utils;
import main.sdis.message.Message;
import main.sdis.protocol.GetPredHandler;
import main.sdis.protocol.GetSuccHandler;
import main.sdis.protocol.IAmPredHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class MessageReceiver implements Runnable {

    private ChordNode node;
    private ServerSocket socket;
    private ExecutorService executorService;

    public MessageReceiver(ChordNode node, int port) throws IOException {
        this.node = node;
        this.socket = new ServerSocket(port);
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

                System.out.println("MessageReceiver: received message from "
                        + Utils.formatAddress(message.getHeader().getSenderAddress()) + " --> " + message);

                switch (message.getHeader().getMessageType()) {
                    case GETPRED:
                        executorService.execute(new GetPredHandler(node, message, out));
                        break;
                    case GETSUCC:
                        executorService.execute(new GetSuccHandler(node, message, out));
                        break;
                    case IAMPRED:
                        executorService.execute(new IAmPredHandler(node, message));
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
