package com.epam.esm.web.exception;

import com.epam.esm.repository.exception.NotImplementedRepositoryException;
import com.epam.esm.service.exception.NotFoundResourceException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotImplementedRepositoryException.class)
    public ResponseEntity<ErrorInfo> handleNotImplementedRepositoryException(NotImplementedRepositoryException e,
                                                                          WebRequest request) {
        ErrorInfo errorInfo = ErrorInfo.builder()
                .setErrorCode(50010L)
                .setErrorMessage(e.getMessage())
                .setExceptionName(e.getClass().getSimpleName())
                .setContextPath(request.getContextPath())
                .build();
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<ErrorInfo> handleNotFoundResourceException(NotFoundResourceException e, WebRequest request) {
        ErrorInfo errorInfo = ErrorInfo.builder()
                .setErrorCode(40410L)
                .setErrorMessage(e.getMessage())
                .setExceptionName(e.getClass().getSimpleName())
                .setContextPath(request.getContextPath())
                .build();
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNotImplementedException(RuntimeException e, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleOthers(Exception e, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
