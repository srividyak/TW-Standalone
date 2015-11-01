package com.tapwisdom.core.common.exception;

/**
 * Created by srividyak on 18/04/15.
 */
public class TapWisdomException extends Exception {
    
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public TapWisdomException(String message, Throwable cause) {
        super(message, cause);
    }

    public TapWisdomException(String message) {
        super(message);
    }
    
    public TapWisdomException(int statusCode, String message) {
        super(message);
        this.setStatusCode(statusCode);
        
    }

    public TapWisdomException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.setStatusCode(statusCode);
    }
}
