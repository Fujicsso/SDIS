package main.sdis.server;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import main.sdis.common.Utils;
import main.sdis.file.FileId;

public class Storage {
    
    private static final String BASE_DIR = "server" + File.separatorChar;
    private static final String DATA_DIRECTORY = BASE_DIR + "data" + File.separatorChar;
    private static final String BACKED_UP_FILES_FILE = "backed_up_files";

    // Stores all files that have been backed up in the system
    // and the list of peers who saved each file
    private Map<FileId, List<InetSocketAddress>> backedUpFiles;

    public Storage() {
        File dataDir = new File(DATA_DIRECTORY);
        this.backedUpFiles = new ConcurrentHashMap<>();

        loadBackedUpFiles();

        dataDir.mkdirs();
    }

    public synchronized void addBackedUpFile(FileId fileId, InetSocketAddress peerAddress) {
        List<InetSocketAddress> peers = backedUpFiles.computeIfAbsent(fileId,
                id -> Collections.synchronizedList(new ArrayList<>()));

        if (!peers.contains(peerAddress))
            peers.add(peerAddress);

        saveBackedUpFiles();

        Utils.safePrintln("File: " + fileId + " Rep: " + getFileReplicationDegree(fileId));
    }

    public synchronized boolean hasBackedUpFile(FileId fileId) {
        return backedUpFiles.containsKey(fileId);
    }

    public synchronized List<InetSocketAddress> getPeersOfBackedUpFile(FileId fileId) {
        return backedUpFiles.get(fileId);
    }

    public synchronized int getFileReplicationDegree(FileId fileId) {
        return backedUpFiles.get(fileId).size();
    }

    private void loadBackedUpFiles() {
        File file = new File(DATA_DIRECTORY + BACKED_UP_FILES_FILE);

        Object deserialized = Utils.deserializeObject(file);

        if (deserialized != null)
            backedUpFiles = (ConcurrentHashMap<FileId, List<InetSocketAddress>>) deserialized;

        System.out.println(Arrays.asList(backedUpFiles));
    }

    private void saveBackedUpFiles() {
        File file = new File(DATA_DIRECTORY + BACKED_UP_FILES_FILE);
        Utils.serializeObject(file, backedUpFiles);
    }

    public synchronized void removeBackedUpFile(FileId fileId, InetSocketAddress peerAddress) {
        List<InetSocketAddress> peers = backedUpFiles.get(fileId);

        if (peers.contains(peerAddress))
            peers.remove(peerAddress);

        backedUpFiles.put(fileId, peers);
        
        saveBackedUpFiles();

        Utils.safePrintln("File: " + fileId + " Rep: " + getFileReplicationDegree(fileId));
    }
}