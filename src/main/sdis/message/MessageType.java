package main.sdis.message;

public enum MessageType {
    PUTFILE,    // Request a peer to store a file
    STORED,     // Sent after a peer stores a file (response to PUTFILE)
    GETFILE,    // Ask for a file
    FILE,       // Send a requested file (response to GETFILE)
    DELETE,     // Delete a file
    REMOVED,    // Sent after a file is deleted by the reclaim protocol
    ALIVE,      // Tells a node that the node who sent the message is still up
    CONNECT,    // Sent by a peer to connect to the server
    CONNECTED,  // Sent by the server to a peer after a CONNECT request
    PING,       // Used to check if a given node is up
    PONG,       // Reply to PING message
    OK          // Generic reply message to messages that do not require a return value
}
