package main.sdis.protocol;

import java.io.ObjectOutputStream;

import main.sdis.chord.ChordNode;
import main.sdis.message.Message;
import main.sdis.peer.MessageSender;

public class Handler {
    
    protected ChordNode node;
    protected Message message;
    protected ObjectOutputStream out;
    protected MessageSender messageSender;

    /**
     * Creates an Handler to handle received messages that do not require a reply
     * @param node the chord node which received the message
     * @param message the received message
     */
    public Handler(ChordNode node, Message message) {
        this.node = node;
        this.message = message;
        this.messageSender = new MessageSender();
    }

    /**
     * Creates an Handler to handle received messages that require a reply
     * @param node the chord node which received the message
     * @param message the received message
     * @param out the ObjectOutputStream of the socket to reply to
     */
    public Handler(ChordNode node, Message message, ObjectOutputStream out) {
        this.node = node;
        this.message = message;
        this.out = out;
        this.messageSender = new MessageSender();
    }
}