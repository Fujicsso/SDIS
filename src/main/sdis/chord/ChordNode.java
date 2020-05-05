package main.sdis.chord;

import java.net.InetSocketAddress;

public interface ChordNode {

    /**
     * Returns the address of the node's sucessor by searching its local finger tabke
     * @return the address of the node's successor
     */
    InetSocketAddress getSuccessor();

    /**
     * Returns the address of the node's predecessor
     * @return the address of the node's predecessor
     */
    InetSocketAddress getPredecessor();

    /**
     * Finds the node which is responsible for a given key, e.g. a file
     * @param key the key to lookup
     * @return the key of the node which is responsible for the given key
     */
    NodeKey lookupKey(Key key);

    /**
     * Returns the finger table entry on the given index
     * @param idx the index
     * @return the finger table entry on index idx
     */
    FingerTableEntry getFingerTableEntry(int idx);

    /**
     * Sets the finger table entry on index idx
     * @param entry the entry by which the entry at index idx is to be replaced
     */
    void setFingerTableEntry(FingerTableEntry entry);
}