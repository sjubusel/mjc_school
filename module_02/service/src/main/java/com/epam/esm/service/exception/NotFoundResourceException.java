package com.epam.esm.service.exception;

import java.io.Serializable;

public class NotFoundResourceException extends ServiceException {
    public NotFoundResourceException() {
    }

    public <T extends Serializable> NotFoundResourceException(T resourceId){
        super(String.format("Requested resource is not found (resource's id = %s", resourceId));
    }

    public NotFoundResourceException(String message) {
        super(message);
    }

    public NotFoundResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundResourceException(Throwable cause) {
        super(cause);
    }
}
