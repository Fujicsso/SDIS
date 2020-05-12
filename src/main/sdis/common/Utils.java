package main.sdis.common;

import main.sdis.file.FileId;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
    
    public static String formatAddress(InetSocketAddress address) {
        return address.toString() + ":" + address.getPort();
    }

    public static String formatAddress(InetAddress address, int port) {
        return address.toString() + ":" + port;
    }

    public static <T> List<T> randomSubList(List<T> list, int newSize) {
        list = new ArrayList<>(list);
        Collections.shuffle(list);
        return list.subList(0, newSize);
    }
}
