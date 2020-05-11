package main.sdis.server;

import java.net.InetSocketAddress;

public class Connection {
    
    private InetSocketAddress clientAddress;

    public Connection(InetSocketAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Connection)) {
            return false;
        }
        Connection connection = (Connection) o;

        return clientAddress.equals(connection.clientAddress);
    }

    @Override
    public String toString() {
        return clientAddress.toString();
    }

}