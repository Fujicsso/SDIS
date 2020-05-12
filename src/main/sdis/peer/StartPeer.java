package main.sdis.peer;

import main.sdis.common.Utils;

import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class StartPeer {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");

        try {
            String accessPoint = args[0];
            int port = Integer.parseInt(args[1]);
            String serverHostname = args[2];
            int serverPort = Integer.parseInt(args[3]);

            // RMI Registry should be run on the root directory of .class files
            Registry registry = LocateRegistry.getRegistry();

            InetSocketAddress address = new InetSocketAddress(port);

            InetSocketAddress serverAddress = new InetSocketAddress(serverHostname, serverPort);

            Peer peer = new PeerImpl(accessPoint, address, serverAddress);
            
            Peer stub = (Peer) UnicastRemoteObject.exportObject(peer, 0);

            registry.rebind(accessPoint, stub);

            Utils.safePrintf("Peer Ready on Access Point %s\n", peer.getAccessPoint());
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
