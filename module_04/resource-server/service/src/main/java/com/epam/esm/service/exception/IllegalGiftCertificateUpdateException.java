package com.epam.esm.service.exception;

public class IllegalGiftCertificateUpdateException extends ServiceException {
    public IllegalGiftCertificateUpdateException() {
    }

    public IllegalGiftCertificateUpdateException(String message) {
        super(message);
    }

    public IllegalGiftCertificateUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalGiftCertificateUpdateException(Throwable cause) {
        super(cause);
    }
}
