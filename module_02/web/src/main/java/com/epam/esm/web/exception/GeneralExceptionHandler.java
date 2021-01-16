package com.epam.esm.web.exception;

import com.epam.esm.repository.exception.NotImplementedRepositoryException;
import com.epam.esm.service.exception.EmptyUpdateException;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import com.epam.esm.service.exception.NoIdentifiableUpdateException;
import com.epam.esm.service.exception.NotFoundResourceException;
import com.epam.esm.service.exception.ResourceAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GeneralExceptionHandler {

    @ExceptionHandler(NotImplementedRepositoryException.class)
    public ResponseEntity<ErrorInfo> handleNotImplementedRepositoryException(NotImplementedRepositoryException e,
                                                                             HttpServletRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(50010L, e, request.getRequestURI());
        log.error("Not implemented method is used on the repository layer: errorInfo → {}; " +
                "exception → {}; webRequest → {}", errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<ErrorInfo> handleNotFoundResourceException(NotFoundResourceException e,
                                                                     HttpServletRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(40410L, e, request.getRequestURI());
        log.error("The requested resource is not found: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e,
                                                                          HttpServletRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(40010L, e, request.getRequestURI());
        log.error("An attempt to create an already existing resource: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncompatibleSearchCriteriaException.class)
    public ResponseEntity<Object> handleIncompatibleSearchCriteriaException(IncompatibleSearchCriteriaException e,
                                                                            HttpServletRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(50020L, e, request.getRequestURI());
        log.error("Incompatible search criteria is passed: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoIdentifiableUpdateException.class)
    public ResponseEntity<Object> handleNoIdentifiableUpdateException(NoIdentifiableUpdateException e,
                                                                      HttpServletRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(50030L, e, request.getRequestURI());
        log.error("An entity without id is called to be updated: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyUpdateException.class)
    public ResponseEntity<Object> handleEmptyUpdateException(EmptyUpdateException e, HttpServletRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(40020L, e, request.getRequestURI());
        log.error("No changes are passed to update an entity: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e,
                                                                     HttpServletRequest request) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        e.getConstraintViolations().forEach(violation -> errorMessageBuilder
                .append(violation.getRootBeanClass().getName())
                .append(" → ")
                .append(violation.getPropertyPath())
                .append(": ")
                .append(violation.getMessage())
                .append("; "));

        ErrorInfo errorInfo = generateStandardErrorInfo(40030L, new String(errorMessageBuilder), e,
                request.getRequestURI());
        log.error("Incompatible parameters are passed: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                           HttpServletRequest request) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error -> errorMessageBuilder
                .append(error.getObjectName())
                .append(": ")
                .append(error.getDefaultMessage())
                .append("; "));

        ErrorInfo errorInfo = generateStandardErrorInfo(40040L, new String(errorMessageBuilder), e,
                request.getRequestURI());
        log.error("Incompatible parameters are passed: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
                                                                      HttpServletRequest request) {
        String errorMessage = e.getName() + " should be of type " + Objects.requireNonNull(e.getRequiredType())
                .getName();
        ErrorInfo errorInfo = generateStandardErrorInfo(40050L, errorMessage, e, request.getRequestURI());
        log.error("Unexpected argument of a method is called: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorInfo> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
                                                                          HttpServletRequest request) {
        String errorMessage = e.getParameterName() + " parameter is missing";
        ErrorInfo errorInfo = generateStandardErrorInfo(40060L, errorMessage, e, request.getRequestURI());
        log.error("A request parameter is absent: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                         HttpServletRequest request) {
        String errorMessage = e.getMethod() + " method is not supported for this request.";
        ErrorInfo errorInfo = generateStandardErrorInfo(40510L, errorMessage, e, request.getRequestURI());
        log.error("Incompatible http method is called: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleOthers(RuntimeException e, HttpServletRequest request) {
        ErrorInfo errorInfo = generateStandardErrorInfo(50040L, e, request.getRequestURI());
        log.error("An unexpected exception occurs: errorInfo → {}; exception → {}; webRequest → {}",
                errorInfo, e, request);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorInfo generateStandardErrorInfo(Long customErrorCode, Exception e, String uri) {
        return ErrorInfo.builder()
                .setErrorCode(customErrorCode)
                .setErrorMessage(e.getMessage())
                .setExceptionName(e.getClass().getSimpleName())
                .setUri(uri)
                .build();
    }

    private ErrorInfo generateStandardErrorInfo(Long errorCode, String errorMessage, Exception e, String uri) {
        return ErrorInfo.builder()
                .setErrorCode(errorCode)
                .setErrorMessage(errorMessage)
                .setExceptionName(e.getClass().getSimpleName())
                .setUri(uri)
                .build();
    }

}
