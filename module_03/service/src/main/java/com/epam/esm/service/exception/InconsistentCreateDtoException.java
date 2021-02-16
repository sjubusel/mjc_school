package com.epam.esm.service.exception;

public class InconsistentCreateDtoException extends ServiceException {
    public InconsistentCreateDtoException() {
    }

    public InconsistentCreateDtoException(String message) {
        super(message);
    }

    public InconsistentCreateDtoException(String message, Throwable cause) {
        super(message, cause);
    }

    public InconsistentCreateDtoException(Throwable cause) {
        super(cause);
    }
}
