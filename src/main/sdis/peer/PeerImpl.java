package main.sdis.peer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.sdis.common.ConnectionFailedException;
import main.sdis.common.MessageSender;
import main.sdis.common.NodeImpl;
import main.sdis.file.FileId;
import main.sdis.message.ConnectMessage;
import main.sdis.message.ConnectedMessage;
import main.sdis.message.PutFileMessage;

public class PeerImpl extends NodeImpl implements Peer {

    private String accessPoint;
    private InetSocketAddress serverAddress;
    private MessageSender messageSender;

    public PeerImpl(String accessPoint, InetSocketAddress address, InetSocketAddress serverAddress)
            throws IOException, ConnectionFailedException {
        super(address);
        this.accessPoint = accessPoint;
        this.serverAddress = serverAddress;
        messageSender = new MessageSender();

        connect(serverAddress);

        executorService.execute(new MessageReceiver(this));
    }

    private void connect(InetSocketAddress serverAddress) throws ConnectionFailedException {
        ConnectMessage message = new ConnectMessage(address);

        ConnectedMessage response = messageSender.<ConnectedMessage>sendMessage(message, serverAddress.getAddress(),
                serverAddress.getPort());

        if (response == null)
            throw new ConnectionFailedException("Could not connect to server.");
    }

    @Override
    public String getAccessPoint() {
        return accessPoint;
    }

    @Override
    public void backupFile(String filePath, int replicationDegree) throws IOException {
        Path path = Paths.get(filePath);
        File file = new File(filePath);

        String owner = Files.getOwner(path).getName();
        long lastModified = file.lastModified();

        byte[] fileBytes = Files.readAllBytes(path);

        FileId fileId = new FileId(file.getName(), lastModified, owner, fileBytes);

        PutFileMessage message = new PutFileMessage(address, fileId, fileBytes, replicationDegree);

        messageSender.sendMessage(message, serverAddress.getAddress(), serverAddress.getPort());
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
