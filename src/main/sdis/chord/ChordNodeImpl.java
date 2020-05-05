package main.sdis.chord;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chord node
 */
public class ChordNodeImpl {

    protected InetSocketAddress address;
    protected InetSocketAddress contact;
    protected InetSocketAddress predecessor;
    protected List<FingerTableEntry> fingerTable;
    protected NodeKey nodeKey;

    /**
     * Creates a new chord node and joins an empty ring
     * @param address The node's InetSocketAddress
     */
    public ChordNodeImpl(InetSocketAddress address) {
        this.address = address;
        nodeKey = new NodeKey(address);
        initFingerTable();

        fingerTable.set(0, new FingerTableEntry(nodeKey, address));
        predecessor = address;
    }

    /**
     * Creates a new chord node and joins an existing ring
     * @param address The node's InetSocketAddress
     * @param contact The contact (bootstrap) node's InetSocketAddress
     */
    public ChordNodeImpl(InetSocketAddress address, InetSocketAddress contact) {
        this.address = address;
        this.contact = contact;
        nodeKey = new NodeKey(address);
        initFingerTable();
    }

    private void initFingerTable() {
        this.fingerTable = new ArrayList<>(ChordSettings.M);
        for (int i = 0; i < fingerTable.size(); i++) {
            fingerTable.set(i, new FingerTableEntry());
        }
    }

}
