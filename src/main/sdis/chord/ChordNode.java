package main.sdis.chord;

import java.net.InetSocketAddress;

public interface ChordNode {
    
    /**
     * Returns the node's address
     * @return the node's InetSocketAddress
     */
    InetSocketAddress getAddress();

    /**
     * Returns the node's key
     * @return the node's key
     */
    Key getKey();

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
     * Searches the node's finger table for the given address and returns the corresponding key
     * @param address the address to search for
     * @return the key that matches the given address
     */
    Key getKeyByAddress(InetSocketAddress address);

    /**
     * Find the sucessor of a given key
     * @param key the key to lookup
     * @return the InetSocketAddres of the sucessor node of the given key
     */
    InetSocketAddress findSuccessor(Key key);

    /**
     * Tell the node its new predecessor is the node with the address nodeAddress
     * @param nodeAddress the address of the new predecessor
     */
    void notify(InetSocketAddress nodeAddress);
}