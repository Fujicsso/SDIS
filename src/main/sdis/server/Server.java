package main.sdis.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import main.sdis.common.NodeImpl;
import main.sdis.common.Utils;
import main.sdis.file.FileId;

public class Server extends NodeImpl {
    
    private List<Connection> connections;

    // Stores all files that have been backed up in the system
    // and the list of peers who saved each file
    private Map<FileId, List<InetSocketAddress>> backedUpFiles;

    public Server(InetSocketAddress address) throws IOException {
        super(address);
        this.connections = Collections.synchronizedList(new ArrayList<>());
        this.backedUpFiles = new ConcurrentHashMap<>();

        executorService.execute(new RequestReceiver(this));
        executorService.scheduleAtFixedRate(new ConnectionMonitor(this), 0, 10, TimeUnit.SECONDS);
    }

    public synchronized void addConnection(Connection connection) {
        connections.add(connection);
    }

    public synchronized void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public synchronized void addBackedUpFile(FileId fileId, InetSocketAddress peerAddress) {
        List<InetSocketAddress> peers = backedUpFiles.computeIfAbsent(fileId,
                id -> Collections.synchronizedList(new ArrayList<>()));

        if (!peers.contains(peerAddress))
            peers.add(peerAddress);

        Utils.safePrintln("File: " + fileId + " Rep: " + getFileReplicationDegree(fileId));
    }

    public synchronized boolean hasBackedUpFile(FileId fileId) {
        return backedUpFiles.containsKey(fileId);
    }

    public synchronized int getFileReplicationDegree(FileId fileId) {
        return backedUpFiles.get(fileId).size();
    }
}