package com.epam.esm.service.exception;

public class IllegalGiftCertificateUpdate extends ServiceException {
    public IllegalGiftCertificateUpdate() {
    }

    public IllegalGiftCertificateUpdate(String message) {
        super(message);
    }

    public IllegalGiftCertificateUpdate(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalGiftCertificateUpdate(Throwable cause) {
        super(cause);
    }
}
