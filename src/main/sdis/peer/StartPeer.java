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
            int identifier = Integer.parseInt(args[0]);
            String accessPoint = args[1];
            int port = Integer.parseInt(args[2]);

            // RMI Registry should be run on the root directory of .class files
            Registry registry = LocateRegistry.getRegistry();

            InetSocketAddress address = new InetSocketAddress(port);

            PeerImpl peer = null;
            if (args.length == 3) {
                peer = new PeerImpl(identifier, accessPoint, address);
            } else {
                String contactHostname = args[3];
                int contactPort = Integer.parseInt(args[4]);

                InetSocketAddress contactAddress = new InetSocketAddress(contactHostname, contactPort);

                peer = new PeerImpl(identifier, accessPoint, address, contactAddress);
            }
            
            Peer stub = (Peer) UnicastRemoteObject.exportObject(peer, 0);

            // for testing purposes only
            // should be "bind"
            registry.rebind(accessPoint, stub);

            Utils.safePrintf("Peer %d Ready on Access Point %s\n", peer.getIdentifier(), peer.getAccessPoint());
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
