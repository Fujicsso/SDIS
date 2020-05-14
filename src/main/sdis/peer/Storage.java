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
import java.util.List;

import main.sdis.common.Utils;
import main.sdis.file.FileId;

public class Storage {

    private static final long DEFAULT_DISK_SIZE = 1000000000L;
    private static final String BACKUP_DIRECTORY = "backup" + File.separatorChar;
    private static final String RESTORED_DIRECTORY = "restore" + File.separatorChar;
    private static final String DATA_DIRECTORY = "data" + File.separatorChar;
    private static final String SAVED_FILES_FILE = "backed_up_files";

    private String storageDir;
    private long maxDiskSize;
    private List<FileId> savedFiles;
    
    public Storage(InetSocketAddress peerAddress) {
        maxDiskSize = DEFAULT_DISK_SIZE;
        storageDir = Utils.formatAddress(peerAddress) + File.separatorChar;
        savedFiles = Collections.synchronizedList(new ArrayList<>());

        File backupDir = new File(storageDir + BACKUP_DIRECTORY);
        File restoreDir = new File(storageDir + RESTORED_DIRECTORY);
        File dataDir = new File(storageDir + DATA_DIRECTORY);
        backupDir.mkdirs();
        restoreDir.mkdirs();
        dataDir.mkdirs();

        loadSavedFiles();
    }

    public void setMaxDiskSize(long maxDiskSize) {
        this.maxDiskSize = maxDiskSize;
    }

    public long getMaxDiskSize() {
        return maxDiskSize;
    }

    public synchronized void saveFile(FileId fileId, byte[] fileData) {
        File file = Utils.createFileAndDir(storageDir + BACKUP_DIRECTORY, fileId.toString());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(fileData);

            savedFiles.add(fileId);
            saveSavedFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSavedFiles() {
        File file = new File(storageDir + DATA_DIRECTORY + SAVED_FILES_FILE);

        Object deserialized = Utils.deserializeObject(file);

        if (deserialized != null)
            savedFiles = (List<FileId>) deserialized;

        System.out.println(Arrays.toString(savedFiles.toArray()));
    }

    private void saveSavedFiles() {
        File file = new File(storageDir + DATA_DIRECTORY + SAVED_FILES_FILE);
        Utils.serializeObject(file, savedFiles);
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
}