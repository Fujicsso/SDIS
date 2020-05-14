package main.sdis.server;

import java.io.File;
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

    public List<Connection> getConnections() {
        return connections;
    }

    public Storage getStorage() {
        return storage;
    }
}