package main.sdis.chord;

import java.net.InetSocketAddress;

import main.sdis.common.Utils;

public class FingerFixer implements Runnable {

    private ChordNode node;

    public FingerFixer(ChordNode node) {
        this.node = node;
    }

    @Override
    public void run() {
        if (node.getFingerToFix() > ChordSettings.M - 1)
            node.resetFingerToFix();

        long nodeKeyValue = node.getKey().getValue();
        Key keyToSearch = new NodeKey(nodeKeyValue + (long) Math.pow(2, node.getFingerToFix() - 1));
        InetSocketAddress fixedAddress = node.findSuccessor(keyToSearch);
        Key fixedKey = new NodeKey(fixedAddress);

        FingerTableEntry fixedFinger = new FingerTableEntry(fixedKey, fixedAddress);
        node.setFingerTableEntry(fixedFinger, node.getFingerToFix());

        node.incrementFingerToFix();

        Utils.printFingerTable(node.getFingerTable());
    }
    
}