package com.epam.esm.service.exception;

public class EmptyUpdateException extends ServiceException {
    public EmptyUpdateException() {
    }

    public EmptyUpdateException(String message) {
        super(message);
    }

    public EmptyUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyUpdateException(Throwable cause) {
        super(cause);
    }
}
