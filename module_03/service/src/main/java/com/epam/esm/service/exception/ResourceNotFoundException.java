package com.epam.esm.service.exception;

import java.io.Serializable;

public class ResourceNotFoundException extends ServiceException {
    public ResourceNotFoundException() {
    }

    public <T extends Serializable> ResourceNotFoundException(T resourceId){
        super(String.format("Requested resource is not found (resource's id = %s)", resourceId));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
