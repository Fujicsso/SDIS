package main.sdis.peer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import main.sdis.common.Utils;
import main.sdis.file.FileId;

public class Storage {

    private static final long DEFAULT_DISK_SIZE = 1000000000L;
    private static final String BACKUP_DIRECTORY = "backup" + File.separatorChar;
    private static final String RESTORED_DIRECTORY = "restore" + File.separatorChar;
    private static final String BACKED_UP_FILES_FILE = "backed_up_files";

    private String storageDir;
    private long maxDiskSize;
    private List<FileId> savedFiles;
    
    public Storage(InetSocketAddress peerAddress) {
        maxDiskSize = DEFAULT_DISK_SIZE;
        storageDir = Utils.formatAddress(peerAddress) + File.separatorChar;
        savedFiles = new ArrayList<>();

        File backupDir = new File(storageDir + BACKUP_DIRECTORY);
        File restoreDir = new File(storageDir + RESTORED_DIRECTORY);
        backupDir.mkdirs();
        restoreDir.mkdirs();
    }

    public void setMaxDiskSize(long maxDiskSize) {
        this.maxDiskSize = maxDiskSize;
    }

    public long getMaxDiskSize() {
        return maxDiskSize;
    }

    public synchronized void saveFile(FileId fileId, byte[] fileData) {
        File file = Utils.createFileAndDir(storageDir + BACKUP_DIRECTORY, fileId.getFileName());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(fileData);

            savedFiles.add(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}