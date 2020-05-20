package main.sdis.peer;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Peer extends Remote {
    // these methods are called by the Peer Client and thus are executed by the
    // Initiator Peer
    void backupFile(String filePath, int replicationDegree) throws IOException;

    void restoreFile(String filePath) throws RemoteException, IOException;

    void deleteFile(String filePath) throws IOException;

    void reclaimSpace(long maxDiskSpace) throws RemoteException, IOException;

    void retrieveState() throws RemoteException;

    String getAccessPoint() throws RemoteException;
}
