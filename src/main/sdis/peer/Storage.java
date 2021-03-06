package main.sdis.peer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import main.sdis.common.Utils;
import main.sdis.file.FileId;

public class Storage {

    private static final long DEFAULT_DISK_SIZE = 1000000000L;
    private static final String BACKUP_DIRECTORY = "backup" + File.separatorChar;
    private static final String RESTORED_DIRECTORY = "restore" + File.separatorChar;
    private static final String DATA_DIRECTORY = "data" + File.separatorChar;
    private static final String SAVED_FILES_FILE = "saved_up_files";
    private static final String BACKED_UP_FILES_FILE = "backed_up_files";

    private String storageDir;
    private long maxDiskSize;
    // Files saved by the peer whose backup was requested by other peers and their
    // desired replication degree
    private Map<FileId, Integer> savedFiles;
    // Files whose backup was initiated by the peer and their desired replication
    // degree
    private Map<FileId, Integer> backedUpFiles;

    public Storage(InetSocketAddress peerAddress) {
        maxDiskSize = DEFAULT_DISK_SIZE;
        storageDir = "peer_" + Utils.generatePeerDirectory(peerAddress) + File.separatorChar;
        savedFiles = new ConcurrentHashMap<>();
        backedUpFiles = new ConcurrentHashMap<>();

        File backupDir = new File(storageDir + BACKUP_DIRECTORY);
        File restoreDir = new File(storageDir + RESTORED_DIRECTORY);
        File dataDir = new File(storageDir + DATA_DIRECTORY);
        backupDir.mkdirs();
        restoreDir.mkdirs();
        dataDir.mkdirs();

        loadSavedFiles();
        loadBackedUpFiles();
    }

    public void setMaxDiskSize(long maxDiskSize) {
        this.maxDiskSize = maxDiskSize;
    }

    public long getMaxDiskSize() {
        return maxDiskSize;
    }

    public Map<FileId, Integer> getSavedFiles() {
        return savedFiles;
    }

    public synchronized void saveFile(FileId fileId, byte[] fileData, int repDegree) {
        File file = Utils.createFileAndDir(storageDir + BACKUP_DIRECTORY, fileId.toString());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(fileData);

            savedFiles.put(fileId, repDegree);
            saveSavedFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSavedFiles() {
        File file = new File(storageDir + DATA_DIRECTORY + SAVED_FILES_FILE);

        Object deserialized = Utils.deserializeObject(file);

        if (deserialized != null)
            savedFiles = (ConcurrentHashMap<FileId, Integer>) deserialized;

        System.out.println(Arrays.toString(Arrays.asList(savedFiles).toArray()));
    }

    private void saveSavedFiles() {
        File file = new File(storageDir + DATA_DIRECTORY + SAVED_FILES_FILE);
        Utils.serializeObject(file, savedFiles);
    }

    private void loadBackedUpFiles() {
        File file = new File(storageDir + DATA_DIRECTORY + BACKED_UP_FILES_FILE);

        Object deserialized = Utils.deserializeObject(file);

        if (deserialized != null)
            backedUpFiles = (ConcurrentHashMap<FileId, Integer>) deserialized;

        System.out.println(Arrays.toString(Arrays.asList(backedUpFiles).toArray()));
    }

    private void saveBackedUpFiles() {
        File file = new File(storageDir + DATA_DIRECTORY + BACKED_UP_FILES_FILE);
        Utils.serializeObject(file, backedUpFiles);
    }

    public void addBackedUpFile(FileId fileId, int replicationDegree) {
        backedUpFiles.put(fileId, replicationDegree);
        saveBackedUpFiles();
    }

    public synchronized byte[] getFileData(FileId fileId) {
        File file = new File(storageDir + BACKUP_DIRECTORY + fileId.toString());

        try {
            byte[] fileData = Files.readAllBytes(Paths.get(file.getPath()));
            return fileData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void restoreFile(FileId fileId, byte[] fileData) {
        File file = new File(storageDir + RESTORED_DIRECTORY + fileId.getFileName());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(fileData);
            Utils.safePrintln("File " + fileId.getFileName() + " restored successfuly");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void listSavedFiles() {
        File backupDir = new File(storageDir + BACKUP_DIRECTORY);

        File[] files = backupDir.listFiles();

        for (File file : files)
            Utils.safePrintln(file.getName() + " | " + Utils.getKbFileSize(file));
    }

    public Map<FileId, Integer> getBackedUpFiles() {
        return backedUpFiles;
    }

    public void deleteFile(FileId fileId) {
        File file = new File(storageDir + BACKUP_DIRECTORY + fileId.toString());

        file.delete();

        removeSavedChunksOfFile(fileId);

    }

    private synchronized void removeSavedChunksOfFile(FileId fileId) {
        savedFiles.remove(fileId);

        saveSavedFiles();
    }

    public long getUsedSpace() {
        long ret = 0;
        for (FileId id : savedFiles.keySet()) {
            File file = new File(storageDir + BACKUP_DIRECTORY + id.toString());

            ret += file.length();
        }

        return ret;
    }

    public synchronized List<File> listSortedSavedFiles() {
        File backUpDirectory = new File(storageDir + BACKUP_DIRECTORY);
        List<File> files = Arrays.asList(backUpDirectory.listFiles());

        Collections.sort(files, Comparator.comparingLong(File::length));

        return files;
    }

}