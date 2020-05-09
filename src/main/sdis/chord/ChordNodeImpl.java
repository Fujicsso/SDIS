package main.sdis.chord;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.sdis.common.CustomExecutorService;
import main.sdis.common.Utils;
import main.sdis.message.Message;
import main.sdis.message.MessageType;
import main.sdis.message.SingleArgumentHeader;
import main.sdis.peer.MessageReceiver;
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
    protected MessageReceiver messageReceiver;
    protected ScheduledExecutorService executorService;
    protected int fingerToFix = 1;

    /**
     * Creates a new chord node and joins an empty ring
     * 
     * @param address The node's InetSocketAddress
     * @throws IOException
     */
    public ChordNodeImpl(InetSocketAddress address) throws IOException {
        init(address);
    }

    /**
     * Creates a new chord node and joins an existing ring
     * 
     * @param address The node's InetSocketAddress
     * @param contact The contact (bootstrap) node's InetSocketAddress
     * @throws IOException
     */
    public ChordNodeImpl(InetSocketAddress address, InetSocketAddress contact) throws IOException {
        init(address);
        this.contact = contact;
    }

    /**
     * Shared constructor code
     * @param address The node's InetSocketAddress
     * @throws IOException
     */
    private void init(InetSocketAddress address) throws IOException {
        this.address = address;
        nodeKey = new NodeKey(address);
        initFingerTable();

        fingerTable.set(0, new FingerTableEntry(nodeKey, address));
        predecessor = address;

        messageSender = new MessageSender();

        executorService = CustomExecutorService.getInstance();

        executorService.scheduleAtFixedRate(new Stabilizer(this), 0, 2, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new PredecessorChecker(this), 0, 2, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new FingerFixer(this), 0, 2, TimeUnit.SECONDS);

        executorService.execute(new MessageReceiver(this, address.getPort()));
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
    public synchronized FingerTableEntry getFingerTableEntry(int idx) {
        return fingerTable.get(idx);
    }

    @Override
    public synchronized void setFingerTableEntry(FingerTableEntry entry, int idx) {
        fingerTable.set(idx, entry);
    }

    @SuppressWarnings("unchecked")
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
        for (int i = ChordSettings.M - 1; i > 0; i--) {
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

    @Override
    public void notify(InetSocketAddress nodeAddress) {
        Key key = new NodeKey(nodeAddress);
        Key predecessorKey = new NodeKey(predecessor);

        if (predecessor == null || Utils.isKeyInOpenInterval(key, predecessorKey, nodeKey))
            predecessor = nodeAddress;

    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }

    @Override
    public Key getKey() {
        return nodeKey;
    }

    @Override
    public void setPredecessor(InetSocketAddress address) {
        this.predecessor = address;
    }

    @Override
    public int getFingerToFix() {
        return fingerToFix;
    }

    @Override
    public void incrementFingerToFix() {
        fingerToFix++;

    }

    @Override
    public void resetFingerToFix() {
        fingerToFix = 1;
    }
}
