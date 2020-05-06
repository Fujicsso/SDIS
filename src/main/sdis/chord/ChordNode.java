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
     * Finds the object with the given key, e.g. a file
     * @param key the key of the object to find
     * @return the object with the specified key
     */
    <T> T lookupObject(Key key);

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

    /**
     * Find the sucessor of a given key
     * @param key the key to lookup
     * @return the InetSocketAddres of the sucessor node of the given key
     */
    InetSocketAddress findSuccessor(Key key);
}