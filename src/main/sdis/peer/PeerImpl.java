package main.sdis.peer;

import main.sdis.common.CustomExecutorService;
import main.sdis.chord.ChordNodeImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledExecutorService;

public class PeerImpl extends ChordNodeImpl implements Peer {

    private int identifier;
    private String accessPoint;
    private ScheduledExecutorService executorService;
    private MessageReceiver receiver;
    private MessageSender sender;

    /**
     * Construct a new Peer and join an empty Chord ring
     * @param identifier the peer's ID
     * @param accessPoint the peer's access point
     * @param address The node's InetSocketAddress
     * @throws IOException
     */
    public PeerImpl(int identifier, String accessPoint, InetSocketAddress address) throws IOException {
        super(address);
        init(identifier, accessPoint);
    }

    /**
     * Construct a new Peer and join an existing Chord ring
     * @param identifier the peer's ID
     * @param accessPoint the peer's access point
     * @param address The node's InetSocketAddress
     * @param contact The contact (bootstrap) node's InetSocketAddress
     * @throws IOException
     */
    public PeerImpl(int identifier, String accessPoint, InetSocketAddress address, InetSocketAddress contact) throws IOException {
        super(address, contact);
        init(identifier, accessPoint);
    }

    private void init(int identifier, String accessPoint) throws IOException {
        this.identifier = identifier;
        this.accessPoint = accessPoint;
        receiver = new MessageReceiver(this.address.getPort());
        sender = new MessageSender();
        executorService = CustomExecutorService.getInstance();
    }

    @Override
    public int getIdentifier() {
        return identifier;
    }

    @Override
    public String getAccessPoint() {
        return accessPoint;
    }

    @Override
    public void backupFile(String filePath, int replicationDegree) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void restoreFile(String filePath) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void reclaimSpace(long maxDiskSpace) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void retrieveState() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
