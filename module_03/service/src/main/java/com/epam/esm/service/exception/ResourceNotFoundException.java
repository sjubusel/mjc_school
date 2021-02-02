package com.epam.esm.service.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResourceNotFoundException extends ServiceException {
    private Long resourceId;

    public ResourceNotFoundException() {
    }

    public <T extends Serializable> ResourceNotFoundException(T resourceId){
        this.resourceId = ((Long) resourceId);
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
