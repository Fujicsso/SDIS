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
import main.sdis.common.Utils;
import main.sdis.file.FileId;
import main.sdis.message.ConnectMessage;
import main.sdis.message.ConnectedMessage;
import main.sdis.message.ErrorMessage;
import main.sdis.message.FileMessage;
import main.sdis.message.GetFileMessage;
import main.sdis.message.Message;
import main.sdis.message.MessageType;
import main.sdis.message.PutFileMessage;

public class PeerImpl extends NodeImpl implements Peer {

    private String accessPoint;
    private InetSocketAddress serverAddress;
    private MessageSender messageSender;
    private Storage storage;

    public PeerImpl(String accessPoint, InetSocketAddress address, InetSocketAddress serverAddress)
            throws IOException, ConnectionFailedException {
        super(address);
        this.accessPoint = accessPoint;
        this.serverAddress = serverAddress;
        storage = new Storage(address);
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

        Message response = messageSender.sendMessage(message, serverAddress.getAddress(), serverAddress.getPort());

        if (response == null)
            Utils.safePrintln("An unexpected error occured");
        else if (response.getMessageType() == MessageType.OK)
            Utils.safePrintln("Backup successful");
        else if (response.getMessageType() == MessageType.ERROR)
            Utils.safePrintln(((ErrorMessage) response).getErrorDetails());
    }

    @Override
    public void restoreFile(String filePath) throws IOException {
        FileId fileId = Utils.generateFileIdForFile(filePath);

        GetFileMessage message = new GetFileMessage(address, fileId);

        Message response = messageSender.sendMessage(message, serverAddress.getAddress(), serverAddress.getPort());

        if (response == null)
            Utils.safePrintln("An unexpected error occured");
        else if (response.getMessageType() == MessageType.FILE) {
            byte[] fileData = ((FileMessage) response).getFileData();
            storage.restoreFile(fileId, fileData);
        }
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

    public Storage getStorage() {
        return storage;
    }
}
