package main.sdis.chord;

import java.net.InetSocketAddress;

/**
 * Represents an entry on a chord node's finger table
 */
public class FingerTableEntry {

    private Key key;
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
    public FingerTableEntry(Key key, InetSocketAddress address) {
        this.key = key;
        this.address = address;
    }

    public Key getKey() {
        return key;
    }

    public InetSocketAddress getAddress() {
        return address;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FingerTableEntry)) {
            return false;
        }

        FingerTableEntry fingerTableEntry = (FingerTableEntry) o;
        return key.equals(fingerTableEntry.key) && address.equals(fingerTableEntry.address);
    }
}
