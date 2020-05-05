package main.sdis.chord;

import java.net.InetSocketAddress;

/**
 * Represents an entry on a chord node's finger table
 */
public class FingerTableEntry {

    private NodeKey key;
    private InetSocketAddress address;

    /**
     * Constructs an empty finger table entry
     */
    public FingerTableEntry() {};

    /**
     * Constructs a finger table entry
     * @param key the node's key
     * @param address the node's InetSocketAddress
     */
    public FingerTableEntry(NodeKey key, InetSocketAddress address) {
        this.key = key;
        this.address = address;
    }

    public NodeKey getKey() {
        return key;
    }

    public InetSocketAddress getAddress() {
        return address;
    }
}
