package main.sdis.file;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class FileId implements Serializable {

    private static final long serialVersionUID = -7980391741390295314L;

    private byte[] hash;
    private String fileName;

    protected FileId() {
    };

    public FileId(String fileName, long lastModified, String owner, byte[] fileData) {
        try {
            hash = generateId(fileName, lastModified, owner, fileData);
        } catch (NoSuchAlgorithmException e) {
            hash = null;
        }

        this.fileName = fileName;
    }

    public FileId(String base64Hash) {
        this.hash = Base64.getUrlDecoder().decode(base64Hash);
    }

    private byte[] generateId(String fileName, long lastModified, String owner, byte[] fileData)
            throws NoSuchAlgorithmException {
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

    public byte[] getHash() {
        return hash;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;

        FileId other = (FileId) obj;

        return Arrays.equals(hash, other.hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }

    @Override
    public String toString() {
        return Base64.getUrlEncoder().encodeToString(hash);
    }
}
