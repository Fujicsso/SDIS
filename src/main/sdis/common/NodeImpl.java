package main.sdis.common;

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledExecutorService;

public class NodeImpl implements Node {
    
    protected InetSocketAddress address;
    protected MessageSender messageSender;
    protected ScheduledExecutorService executorService;

    public NodeImpl(InetSocketAddress address) {
        this.address = address;
        messageSender = new MessageSender();
        executorService = CustomExecutorService.getInstance();
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }
}