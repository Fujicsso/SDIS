package main.sdis.file;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import main.sdis.chord.ChordSettings;
import main.sdis.chord.Key;
import main.sdis.common.Utils;

public class FileId implements Serializable, Key {

    private static final long serialVersionUID = -7980391741390295314L;

    private long value;

    protected FileId() {};

    public FileId(String fileName, long lastModified, String owner, byte[] fileData) {
        try {
            byte[] hash = generateId(fileName, lastModified, owner, fileData);
            value = Utils.truncateHash(hash, ChordSettings.M);
        } catch (NoSuchAlgorithmException e) {
            this.value = 0;
        }
    }

    public FileId(byte[] hash) {
        value = Utils.truncateHash(hash, ChordSettings.M);
    }

    private byte[] generateId(String fileName, long lastModified, String owner, byte[] fileData) throws NoSuchAlgorithmException {
        String lastModifiedStr = Long.toString(lastModified);

        byte[] fileIdBytes = new byte[fileName.getBytes().length + Long.toString(lastModified).getBytes().length 
            + owner.getBytes().length + fileData.length];
        ByteBuffer buff = ByteBuffer.wrap(fileIdBytes);

        buff.put(fileName.getBytes());
        buff.put(lastModifiedStr.getBytes());
        buff.put(owner.getBytes());
        buff.put(fileData);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hash = digest.digest(buff.array());

        return hash;
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;

        FileId other = (FileId) obj;

        return value == other.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
