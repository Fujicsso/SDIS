package main.sdis.peer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PeerClient {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");

        String accessPoint = args[0];
        String filePath;
        Operation operation;

        try {
            operation = Enum.valueOf(Operation.class, args[1]);
        } catch (IllegalArgumentException e) {
            operation = Operation.INVALID;
        }

        try {
            Registry registry = LocateRegistry.getRegistry();
            Peer stub = (Peer) registry.lookup(accessPoint);

            switch(operation) {
                case BACKUP:
                    filePath = args[2];
                    int replicationDegree = Integer.parseInt(args[3]);
                    stub.backupFile(filePath, replicationDegree);
                    break;
                case RESTORE:
                    filePath = args[2];
                    stub.restoreFile(filePath);
                    break;
                case DELETE:
                    filePath = args[2];
                    stub.deleteFile(filePath);
                    break;
                case RECLAIM:
                    int maxDiskSpace = Integer.parseInt(args[2]);
                    stub.reclaimSpace(maxDiskSpace);
                    break;
                case STATE:
                    stub.retrieveState();
                    break;
                default:
                    System.out.println("Invalid Operation");
                    break;

            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
