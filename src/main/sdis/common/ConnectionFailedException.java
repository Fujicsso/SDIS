package main.sdis.common;

public class ConnectionFailedException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public ConnectionFailedException(String errorMessage) {
        super(errorMessage);
    }
}