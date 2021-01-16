package com.epam.esm.repository.exception;

public class NotImplementedRepositoryException extends RepositoryException {
    public NotImplementedRepositoryException() {
    }

    public NotImplementedRepositoryException(String message) {
        super(message);
    }

    public NotImplementedRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotImplementedRepositoryException(Throwable cause) {
        super(cause);
    }
}
