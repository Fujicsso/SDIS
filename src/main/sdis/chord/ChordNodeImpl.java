package main.sdis.chord;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import main.sdis.common.Utils;
import main.sdis.message.Message;
import main.sdis.message.MessageHeader;
import main.sdis.message.MessageType;
import main.sdis.message.SingleArgumentHeader;
import main.sdis.peer.MessageSender;

/**
 * Represents a chord node
 */
public class ChordNodeImpl implements ChordNode {

    protected InetSocketAddress address;
    protected InetSocketAddress contact;
    protected InetSocketAddress predecessor;
    protected List<FingerTableEntry> fingerTable;
    protected NodeKey nodeKey;
    protected MessageSender messageSender;

    /**
     * Creates a new chord node and joins an empty ring
     * 
     * @param address The node's InetSocketAddress
     */
    public ChordNodeImpl(InetSocketAddress address) {
        this.address = address;
        nodeKey = new NodeKey(address);
        initFingerTable();

        fingerTable.set(0, new FingerTableEntry(nodeKey, address));
        predecessor = address;

        messageSender = new MessageSender();
    }

    /**
     * Creates a new chord node and joins an existing ring
     * 
     * @param address The node's InetSocketAddress
     * @param contact The contact (bootstrap) node's InetSocketAddress
     */
    public ChordNodeImpl(InetSocketAddress address, InetSocketAddress contact) {
        this.address = address;
        this.contact = contact;
        nodeKey = new NodeKey(address);
        initFingerTable();

        messageSender = new MessageSender();
    }

    private void initFingerTable() {
        this.fingerTable = new ArrayList<>();
        for (int i = 0; i < ChordSettings.M; i++) {
            fingerTable.add(new FingerTableEntry());
        }
    }

    @Override
    public InetSocketAddress getSuccessorAddress() {
        return fingerTable.get(0).getAddress();
    }

    @Override
    public Key getSuccessorKey() {
        return fingerTable.get(0).getKey();
    }

    @Override
    public InetSocketAddress getPredecessor() {
        return predecessor;
    }

    @Override
    public <T> T lookupObject(Key key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FingerTableEntry getFingerTableEntry(int idx) {
        return fingerTable.get(idx);
    }

    @Override
    public void setFingerTableEntry(FingerTableEntry entry, int idx) {
        fingerTable.set(idx, entry);
    }

    @Override
    public InetSocketAddress findSuccessor(Key key) {
        if (Utils.isKeyInInterval(key, this.nodeKey, getSuccessorKey()))
            return getSuccessorAddress();
        else {
            InetSocketAddress closestPrecedingNode = getClosestPrecedingNode(key);
            Key closestPrecedingKey = getKeyByAddress(closestPrecedingNode);

            SingleArgumentHeader<Key> header = new SingleArgumentHeader<Key>(MessageType.GETSUCC, address,
                    closestPrecedingKey);

            Message message = new Message(header);

            Message responseMessage = messageSender.sendMessage(message, closestPrecedingNode.getAddress(),
                    closestPrecedingNode.getPort());

            SingleArgumentHeader<InetSocketAddress> responseHeader = (SingleArgumentHeader<InetSocketAddress>) responseMessage
                    .getHeader();

            return responseHeader.getArg();
        }
    }

    @Override
    public InetSocketAddress getClosestPrecedingNode(Key key) {
        for (int i = ChordSettings.M; i > 1; i--) {
            if (Utils.isKeyInOpenInterval(getFingerTableEntry(i).getKey(), nodeKey, key))
                return getFingerTableEntry(i).getAddress();

        }
        return address;
    }

    @Override
    public Key getKeyByAddress(InetSocketAddress address) {
        for (FingerTableEntry entry : fingerTable) {
            if (entry.getAddress().equals(address))
                return entry.getKey();
        }

        return null;
    }

}
