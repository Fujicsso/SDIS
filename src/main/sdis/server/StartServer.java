package main.sdis.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import main.sdis.common.Utils;

public class StartServer {
    
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);

        try {
            new Server(new InetSocketAddress(port));
            Utils.safePrintln("Server running on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}