package main.sdis.protocol;

import main.sdis.chord.ChordNode;
import main.sdis.message.Message;
import main.sdis.peer.MessageSender;

public class Handler {
    
    protected ChordNode node;
    protected Message message;
    protected MessageSender messageSender;

    public Handler(ChordNode node, Message message) {
        this.node = node;
        this.message = message;
        this.messageSender = new MessageSender();
    }
}