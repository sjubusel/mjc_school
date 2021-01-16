package com.epam.esm.web.exception;

import com.epam.esm.repository.exception.NotImplementedRepositoryException;
import com.epam.esm.service.exception.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
        ErrorInfo errorInfo = generateStandardErrorInfo(50010L, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<ErrorInfo> handleNotFoundResourceException(NotFoundResourceException e, WebRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(40410L, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e,
                                                                          WebRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(40010L, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncompatibleSearchCriteriaException.class)
    public ResponseEntity<Object> handleOthers(IncompatibleSearchCriteriaException e, WebRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(50020L, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoIdentifiableUpdateException.class)
    public ResponseEntity<Object> handleOthers(NoIdentifiableUpdateException e, WebRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(50030L, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyUpdateException.class)
    public ResponseEntity<Object> handleOthers(EmptyUpdateException e, WebRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(40020L, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    //    @ExceptionHandler({Exception.class})
//    public ResponseEntity<Object> handleOthers(Exception e, WebRequest request) {
//        String bodyOfResponse = "This should be application specific";
//        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
//    }

    private ErrorInfo generateStandardErrorInfo(Long customErrorCode, Exception e, WebRequest request) {
        return ErrorInfo.builder()
                .setErrorCode(customErrorCode)
                .setErrorMessage(e.getMessage())
                .setExceptionName(e.getClass().getSimpleName())
                .setContextPath(request.getContextPath())
                .build();
    }

}
