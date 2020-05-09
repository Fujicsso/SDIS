package main.sdis.common;

import main.sdis.chord.FingerTableEntry;
import main.sdis.chord.Key;
import main.sdis.file.FileId;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static synchronized void safePrintln(Object o) {
        System.out.println(o);
    }

    public static synchronized void safePrintf(String format, Object... args) {
            System.out.printf(format, args);
    }

    public static File createFileAndDir(String directory, String fileName) {
        File dir = new File(directory);

        if (!dir.exists()) dir.mkdirs();

        return new File(directory + File.separatorChar + fileName);
    }

    public static void waitRandomDelay() {
        int delay = new Random(401).nextInt();

        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static FileId generateFileIdForFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        File file = new File(filePath);

        String owner = Files.getOwner(path).getName();
        long lastModified = file.lastModified();

        byte[] fileBytes = Files.readAllBytes(path);

        return new FileId(file.getName(), lastModified, owner, fileBytes);
    }

    public static void serializeObject(File file, Object o) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(o);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Object deserializeObject(File file) {
        Object o = null;

        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            o =  in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return o;
    }

    /**
     * Checks whether a given key belongs to a half closed interval of keys
     * @param key the key to check
     * @param lower the lower bound of the interval (exclusive)
     * @param upper the upper bound of the interval (inclusive)
     * @return true if the key belongs to the interval, false if otherwise
     */
    public static boolean isKeyInInterval(Key key, Key lower, Key upper) {
        return key.getValue() > lower.getValue() && key.getValue() <= upper.getValue();
    }

    /**
     * Checks whether a given key belongs to an open interval of keys
     * @param key the key to check
     * @param lower the lower bound of the interval (exclusive)
     * @param upper the upper bound of the interval (exclusive)
     * @return true if the key belongs to the interval, false if otherwise
     */
    public static boolean isKeyInOpenInterval(Key key, Key lower, Key upper) {
        return key.getValue() > lower.getValue() && key.getValue() < upper.getValue();
    }

    /**
     * Truncates an hash to m bits and returns the respective long value
     * Since the return is of type long, this limits m to 64, since that's the
     * maximum number of bits a long can hold
     * @param hash the hash to truncate
     * @param m number of bits to truncate the hash to
     * @return long value of the truncated hash
     */
    public static long truncateHash(byte[] hash, int m) {
        int necessarySize = (int) Math.ceil((double) m / 8);

        // Truncated key is the size of a long (8 bytes)
        byte[] truncatedKey = new byte[8];
        
        for (int i = 0; i < necessarySize; i++) {
            truncatedKey[i] = hash[i];
        }

        for (int i = necessarySize; i < truncatedKey.length; i++) {
            truncatedKey[i] = 0;
        }

        ByteBuffer buf = ByteBuffer.wrap(truncatedKey);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.rewind();

        return buf.getLong();
    }

    public static String formatAddress(InetSocketAddress address) {
        return address.toString() + ":" + address.getPort();
    }

    public static String formatAddress(InetAddress address, int port) {
        return address.toString() + ":" + port;
    }

    public synchronized static void printFingerTable(List<FingerTableEntry> fingerTable) {
        final String FORMAT = "%-5d%-25s%-25s\n";

        for (int i = 0; i < fingerTable.size(); i++) {
            FingerTableEntry finger = fingerTable.get(i);

            if (finger == null)
                Utils.safePrintf(FORMAT, i, "NULL", "NULL");
            else
                Utils.safePrintf(FORMAT, i, finger.getAddress(), finger.getKey());
        }

        Utils.safePrintln("");
    }
}
