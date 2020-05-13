package main.sdis.common;

import java.io.ObjectOutputStream;

import main.sdis.message.Message;

public class Handler<S extends Node, T extends Message> {
    
    protected S node;
    protected T message;
    protected ObjectOutputStream out;
    protected MessageSender messageSender;

    /**
     * Creates an Handler to handle received messages that do not require a reply
     * @param node the node which received the message
     * @param message the received message
     */
    public Handler(S node, T message) {
        this.node = node;
        this.message = message;
        this.messageSender = new MessageSender();
    }

    /**
     * Creates an Handler to handle received messages that require a reply
     * @param node the node which received the message
     * @param message the received message
     * @param out the ObjectOutputStream of the socket to reply to
     */
    public Handler(S node, T message, ObjectOutputStream out) {
        this.node = node;
        this.message = message;
        this.out = out;
        this.messageSender = new MessageSender();
    }
}