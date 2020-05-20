package main.sdis.common;

import java.io.Serializable;
import java.util.Objects;

import main.sdis.file.FileId;

public class BackupInfo implements Serializable {
    
    private static final long serialVersionUID = -4607046531902407976L;
    private FileId fileId;
    private int desiredRepDegree;

    public BackupInfo(FileId fileId, int desiredRepDegree) {
        this.fileId = fileId;
        this.desiredRepDegree = desiredRepDegree;
    }

    public BackupInfo(FileId fileId) {
        this.fileId = fileId;
    }

    public FileId getFileId() {
        return fileId;
    }

    public int getDesiredRepDegree() {
        return desiredRepDegree;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BackupInfo)) {
            return false;
        }
        BackupInfo backupInfo = (BackupInfo) o;
        return fileId.equals(backupInfo.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId);
    }

}
