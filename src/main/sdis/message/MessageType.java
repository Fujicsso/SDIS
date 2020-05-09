package main.sdis.message;

public enum MessageType {
    PUTFILE,    // Request a peer to store a file
    STORED,     // Sent after a peer stores a file (response to PUTFILE)
    GETFILE,    // Ask for a file
    FILE,       // Send a requested file (response to GETFILE)
    DELETE,     // Delete a file
    REMOVED,    // Sent after a file is deleted by the reclaim protocol
    ALIVE,      // Tells a node that the node who sent the message is still up
    GETSUCC,    // Ask for the address of the successor node of a given key
    SUCC,       // Send the requested successor address (response to GETSUCC)
    GETPRED,    // Ask for the node's predecessor's address
    PRED,       // Send the node's predecessor's address (response to GETPRED)
    IAMPRED,    // Tells a node that this is its new predecessor
    PING,       // Used to check if a given node is up
    PONG,       // Reply to PING message
    OK          // Generic reply message to messages that do not require a return value
}
