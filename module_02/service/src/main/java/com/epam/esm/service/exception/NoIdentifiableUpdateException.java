package com.epam.esm.service.exception;

public class NoIdentifiableUpdateException extends ServiceException {
    public NoIdentifiableUpdateException() {
    }

    public NoIdentifiableUpdateException(String message) {
        super(message);
    }

    public NoIdentifiableUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoIdentifiableUpdateException(Throwable cause) {
        super(cause);
    }
}
