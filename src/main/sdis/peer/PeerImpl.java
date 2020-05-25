package main.sdis.peer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import main.sdis.common.ConnectionFailedException;
import main.sdis.common.MessageSender;
import main.sdis.common.NodeImpl;
import main.sdis.common.Utils;
import main.sdis.file.FileId;
import main.sdis.message.ConnectMessage;
import main.sdis.message.ConnectedMessage;
import main.sdis.message.DeleteMessage;
import main.sdis.message.DeletePeersMessage;
import main.sdis.message.ErrorMessage;
import main.sdis.message.GetDeletePeersMessage;
import main.sdis.message.GetFileMessage;
import main.sdis.message.GetReclaimPeersMessage;
import main.sdis.message.GetRestorePeersMessage;
import main.sdis.message.Message;
import main.sdis.message.MessageType;
import main.sdis.message.PutFileMessage;
import main.sdis.message.ReclaimPeersMessage;
import main.sdis.message.RemovedMessage;
import main.sdis.message.RestorePeersMessage;
import main.sdis.peer.protocol.Backup;
import main.sdis.peer.protocol.Delete;
import main.sdis.peer.protocol.Reclaim;
import main.sdis.peer.protocol.Restore;

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

        PutFileMessage putFileMessage = new PutFileMessage(address, fileId, fileBytes, replicationDegree);

        executorService.execute(new Backup(this, putFileMessage));
    }

    @Override
    public void restoreFile(String filePath) throws IOException {
        FileId fileId = Utils.generateFileIdForFile(filePath);

        GetRestorePeersMessage getRestorePeersMessage = new GetRestorePeersMessage(address, fileId);
        RestorePeersMessage getRestorePeersResponse = messageSender.sendMessage(getRestorePeersMessage,
                serverAddress.getAddress(), serverAddress.getPort());

        GetFileMessage getFileMessage = new GetFileMessage(address, fileId);

        executorService.execute(new Restore(this, getFileMessage, getRestorePeersResponse.getRestorePeers()));
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        FileId fileId = Utils.generateFileIdForFile(filePath);

        DeleteMessage deleteMessage = new DeleteMessage(address, fileId);

        executorService.execute(new Delete(this, deleteMessage));
    }

    @Override
    public void reclaimSpace(long maxDiskSpace) throws IOException {
        storage.setMaxDiskSize(maxDiskSpace);
        List<File> storedFiles = storage.listSortedSavedFiles();
        long usedSpace = storage.getUsedSpace();

        Utils.safePrintf("Used space before reclaim: %dB\n ", usedSpace);

        for (File file : storedFiles) {
            if (usedSpace > maxDiskSpace) {
                usedSpace -= file.length();

                Path path = Paths.get(file.getPath());
                
                byte[] fileBytes = Files.readAllBytes(path);
                FileId fileId = new FileId(file.getName());
                Map<FileId, Integer> mapa = storage.getSavedFiles();
                int desiredRepDegree = mapa.get(fileId);
                storage.deleteFile(fileId);

                GetReclaimPeersMessage grpMessage =new GetReclaimPeersMessage(address,fileId);
                ReclaimPeersMessage getReclaimPeersResponse = messageSender.sendMessage(grpMessage,
                serverAddress.getAddress(), serverAddress.getPort());

                PutFileMessage putFileMessage = new PutFileMessage(address, fileId, fileBytes,desiredRepDegree);

                executorService.execute(new Reclaim(this, putFileMessage, getReclaimPeersResponse.getReclaimPeers()));
            }
        }

        Utils.safePrintf("Used space after reclaim: %dB\n ", usedSpace);

    }

    @Override
    public void retrieveState() {
        Utils.safePrintln("BACKED UP FILES");
        for (Map.Entry<FileId, Integer> entry : storage.getBackedUpFiles().entrySet()) {
            Utils.safePrintln(entry.getKey() + " | Rep. degree = " + entry.getValue());
        }
        Utils.safePrintln("");
        Utils.safePrintln("SAVED FILES");
        storage.listSavedFiles();
    }

    public Storage getStorage() {
        return storage;
    }

    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }
}
