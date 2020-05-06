package main.sdis.chord;

import java.net.InetSocketAddress;

public interface ChordNode {

    /**
     * Returns the address of the node's sucessor by searching its local finger table
     * @return the address of the node's successor
     */
    InetSocketAddress getSuccessorAddress();

    /**
     * Returns the key of the node's successor by searching its local finger table
     * @return the key of the node's sucessor
     */
    Key getSuccessorKey();

    /**
     * Returns the address of the node's predecessor
     * @return the address of the node's predecessor
     */
    InetSocketAddress getPredecessor();

    /**
     * Returns the closest preceding node of the given key
     * @return the address of closest preceding node of the key
     */
    InetSocketAddress getClosestPrecedingNode(Key key);

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
     * @param idx the index
     */
    void setFingerTableEntry(FingerTableEntry entry, int idx);

    /**
     * Find the sucessor of a given key
     * @param key the key to lookup
     * @return the InetSocketAddres of the sucessor node of the given key
     */
    InetSocketAddress findSuccessor(Key key);
}