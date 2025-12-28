package com.app.corechat.exceptions;

public class DuplicateEmailException extends RuntimeException {


    
    public DuplicateEmailException(String msg) {
        super(msg);
    }
    

    public DuplicateEmailException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
