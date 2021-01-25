package com.epam.esm.service.exception;

public class IncompatibleSearchCriteriaException extends ServiceException {
    public IncompatibleSearchCriteriaException() {
    }

    public IncompatibleSearchCriteriaException(String message) {
        super(message);
    }

    public IncompatibleSearchCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompatibleSearchCriteriaException(Throwable cause) {
        super(cause);
    }
}
