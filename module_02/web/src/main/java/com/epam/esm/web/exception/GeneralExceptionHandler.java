package com.epam.esm.web.exception;

import com.epam.esm.repository.exception.NotImplementedRepositoryException;
import com.epam.esm.service.exception.EmptyUpdateException;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import com.epam.esm.service.exception.NoIdentifiableUpdateException;
import com.epam.esm.service.exception.NotFoundResourceException;
import com.epam.esm.service.exception.ResourceAlreadyExistsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GeneralExceptionHandler {

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
    public ResponseEntity<Object> handleIncompatibleSearchCriteriaException(IncompatibleSearchCriteriaException e,
                                                                            WebRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(50020L, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoIdentifiableUpdateException.class)
    public ResponseEntity<Object> handleNoIdentifiableUpdateException(NoIdentifiableUpdateException e,
                                                                      WebRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(50030L, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyUpdateException.class)
    public ResponseEntity<Object> handleEmptyUpdateException(EmptyUpdateException e, WebRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(40020L, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e,
                                                                     WebRequest request) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        e.getConstraintViolations().forEach(violation -> errorMessageBuilder
                .append(violation.getRootBeanClass().getName())
                .append(" → ")
                .append(violation.getPropertyPath())
                .append(": ")
                .append(violation.getMessage())
                .append("; "));

        ErrorInfo errorInfo = generateStandardErrorInfo(40030L, new String(errorMessageBuilder), e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                           WebRequest request) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error -> errorMessageBuilder
                .append(error.getObjectName())
                .append(": ")
                .append(error.getDefaultMessage())
                .append("; "));

        ErrorInfo errorInfo = generateStandardErrorInfo(40050L, new String(errorMessageBuilder), e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
                                                                      WebRequest request) {
        String errorMessage = e.getName() + " should be of type " + Objects.requireNonNull(e.getRequiredType())
                .getName();
        ErrorInfo errorInfo = generateStandardErrorInfo(40040L, errorMessage, e, request);
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

    private ErrorInfo generateStandardErrorInfo(Long errorCode, String errorMessage, Exception e, WebRequest request) {
        return ErrorInfo.builder()
                .setErrorCode(errorCode)
                .setErrorMessage(errorMessage)
                .setExceptionName(e.getClass().getSimpleName())
                .setContextPath(request.getContextPath())
                .build();
    }

}
