package main.sdis.peer;

import java.io.IOException;
import java.net.InetSocketAddress;

import main.sdis.common.ConnectionFailedException;
import main.sdis.common.NodeImpl;
import main.sdis.message.Message;
import main.sdis.message.MessageHeader;
import main.sdis.message.MessageType;

public class PeerImpl extends NodeImpl implements Peer {

    private int identifier;
    private String accessPoint;
    private InetSocketAddress serverAddress;

    public PeerImpl(int identifier, String accessPoint, InetSocketAddress address, InetSocketAddress serverAddress)
            throws IOException, ConnectionFailedException {
        super(address);
        this.identifier = identifier;
        this.accessPoint = accessPoint;
        this.serverAddress = serverAddress;

        connect(serverAddress);

        executorService.execute(new MessageReceiver(this));
    }

    private void connect(InetSocketAddress serverAddress) throws ConnectionFailedException {
        MessageHeader header = new MessageHeader(MessageType.CONNECT, address);
        Message message = new Message(header);

        Message response = messageSender.sendMessage(message, serverAddress.getAddress(), serverAddress.getPort());

        if (response == null || response.getHeader().getMessageType() != MessageType.CONNECTED)
            throw new ConnectionFailedException("Could not connect to server.");
    }

    @Override
    public int getIdentifier() {
        return identifier;
    }

    @Override
    public String getAccessPoint() {
        return accessPoint;
    }

    @Override
    public void backupFile(String filePath, int replicationDegree) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void restoreFile(String filePath) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void reclaimSpace(long maxDiskSpace) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void retrieveState() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
