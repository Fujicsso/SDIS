package main.sdis.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import main.sdis.common.BackupInfo;
import main.sdis.common.NodeImpl;
import main.sdis.common.Utils;
import main.sdis.message.GetBackupPeersMessage;
import main.sdis.message.GetDeletePeersMessage;
import main.sdis.message.GetReclaimPeersMessage;
import main.sdis.message.GetRestorePeersMessage;

public class Server extends NodeImpl {
    
    private List<Connection> connections;
    private Storage storage;
    

    public Server(InetSocketAddress address) throws IOException {
        super(address);
        this.connections = Collections.synchronizedList(new ArrayList<>());
        storage = new Storage();

        executorService.execute(new RequestReceiver(this));
        executorService.scheduleAtFixedRate(new ConnectionMonitor(this), 0, 10, TimeUnit.SECONDS);
    }

    public synchronized void addConnection(Connection connection) {
        if (!connections.contains(connection))
            connections.add(connection);
    }

    public synchronized void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    public synchronized List<Connection> getConnections() {
        return connections;
    }

    public Storage getStorage() {
        return storage;
    }

    public synchronized List<InetSocketAddress> getBackupPeers(GetBackupPeersMessage message) {
        List<InetSocketAddress> peersOfFile = storage.getPeersOfBackedUpFile(message.getFileId());

        List<Connection> randomConnections = new ArrayList<>(connections);
            randomConnections.removeIf(conn -> conn.getClientAddress().equals(message.getSenderAddress())
                    || (peersOfFile != null && peersOfFile.contains(conn.getClientAddress())));
            randomConnections = Utils.randomSubList(randomConnections, message.getReplicationDegree());

        return getConnectionAddresses(randomConnections);
    }

    public synchronized List<InetSocketAddress> getRestorePeers(GetRestorePeersMessage message) {
        List<InetSocketAddress> peers = new ArrayList<>(storage.getPeersOfBackedUpFile(message.getFileId()));
        Collections.shuffle(peers);

        return peers;
    }

    public synchronized List<InetSocketAddress> getDeletePeers(GetDeletePeersMessage message) {
        return storage.getPeersOfBackedUpFile(message.getFileId());
    }

    public synchronized List<InetSocketAddress> getReclaimPeers(GetReclaimPeersMessage message) {
        Map<BackupInfo, List<InetSocketAddress>> mapa = storage.getBackedUpFiles();
        List<InetSocketAddress> peersOfFile = storage.getPeersOfBackedUpFile(message.getFileId());
        BackupInfo bui = Utils.findKey(mapa, new BackupInfo(message.getFileId()));
        List<Connection> reclaimConnections = new ArrayList<>(connections);

        reclaimConnections.removeIf(conn -> conn.getClientAddress().equals(bui.getIniciatorPeer())
        || (peersOfFile != null && peersOfFile.contains(conn.getClientAddress())));

        return getConnectionAddresses(reclaimConnections);
    }

    private List<InetSocketAddress> getConnectionAddresses(List<Connection> connections) {
        List<InetSocketAddress> addresses = new ArrayList<>();

        for (Connection conn : connections) {
            addresses.add(conn.getClientAddress());
        }

        return addresses;
    }
}