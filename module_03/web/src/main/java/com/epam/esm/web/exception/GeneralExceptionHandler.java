package com.epam.esm.web.exception;

import com.epam.esm.service.exception.EmptyUpdateException;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
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
import java.util.Locale;
import java.util.Objects;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class GeneralExceptionHandler {

    public static final String RESOURCE_NOT_FOUND_EXCEPTION_BY_ID = "resource.not.found.exception.by.id";
    public static final String RESOURCE_NOT_FOUND_EXCEPTION_ALL = "resource.not.found.exception.all";
    public static final String DUPLICATE_RESOURCE_EXCEPTION = "duplicate.resource.exception";
    public static final String INCOMPATIBLE_SEARCH_CRITERIA_EXCEPTION = "incompatible.search.criteria.exception";
    public static final String EMPTY_UPDATE_EXCEPTION = "empty.update.exception";
    public static final String CONSTRAINT_VIOLATION_EXCEPTION = "constraint.violation.exception";
    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "method.argument.not.valid.exception";
    public static final String METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION = "method.argument.type.mismatch.exception";
    public static final String MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION
            = "missing.servlet.request.parameter.exception";
    public static final String HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION
            = "http.request.method.not.supported.exception";
    public static final String OTHER_EXCEPTIONS = "other.exceptions";

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleResourceNotFoundException(ResourceNotFoundException e,
                                                                     HttpServletRequest request,
                                                                     Locale locale) {
        String errorMessage;
        if (e.getResourceId() != null) {
            errorMessage = messageSource.getMessage(RESOURCE_NOT_FOUND_EXCEPTION_BY_ID,
                    new Object[]{e.getResourceId().toString()}, locale);
        } else {
            errorMessage = messageSource.getMessage(RESOURCE_NOT_FOUND_EXCEPTION_ALL, null, locale);
        }

        ErrorInfo errorInfo = generateStandardErrorInfo(40410L, errorMessage, e, request.getRequestURI());
        log.error("The requested resource is not found: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorInfo> handleDuplicateResourceException(DuplicateResourceException e,
                                                                      HttpServletRequest request,
                                                                      Locale locale) {
        String errorMessage = messageSource.getMessage(DUPLICATE_RESOURCE_EXCEPTION,
                new Object[]{e.getMessage()}, locale);
        ErrorInfo errorInfo = generateStandardErrorInfo(40010L, errorMessage, e, request.getRequestURI());
        log.error("An attempt to create an already existing resource: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncompatibleSearchCriteriaException.class)
    public ResponseEntity<Object> handleIncompatibleSearchCriteriaException(IncompatibleSearchCriteriaException e,
                                                                            HttpServletRequest request,
                                                                            Locale locale) {
        String errorMessage = messageSource.getMessage(INCOMPATIBLE_SEARCH_CRITERIA_EXCEPTION, null, locale);
        ErrorInfo errorInfo = generateStandardErrorInfo(50010L, errorMessage, e, request.getRequestURI());
        log.error("Incompatible search criteria is passed: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyUpdateException.class)
    public ResponseEntity<Object> handleEmptyUpdateException(EmptyUpdateException e, HttpServletRequest request,
                                                             Locale locale) {
        String errorMessage = messageSource.getMessage(EMPTY_UPDATE_EXCEPTION, new Object[]{e.getMessage()}, locale);
        ErrorInfo errorInfo = generateStandardErrorInfo(40020L, errorMessage, e, request.getRequestURI());
        log.error("No changes are passed to update an entity: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e,
                                                                     HttpServletRequest request,
                                                                     Locale locale) {
        String errorMessageType = messageSource.getMessage(CONSTRAINT_VIOLATION_EXCEPTION, null, locale);
        StringBuilder errorMessageBuilder = new StringBuilder();
        errorMessageBuilder.append(errorMessageType).append(" --- ");
        e.getConstraintViolations().forEach(violation -> errorMessageBuilder
                .append(violation.getRootBeanClass().getName())
                .append(" → ")
                .append(violation.getPropertyPath())
                .append(": ")
                .append(violation.getMessage())
                .append("; "));

        ErrorInfo errorInfo = generateStandardErrorInfo(40030L, new String(errorMessageBuilder), e,
                request.getRequestURI());
        log.error("Incompatible parameters are passed: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                           HttpServletRequest request,
                                                                           Locale locale) {
        String errorMessageType = messageSource.getMessage(METHOD_ARGUMENT_NOT_VALID_EXCEPTION, null, locale);
        StringBuilder errorMessageBuilder = new StringBuilder();
        errorMessageBuilder.append(errorMessageType).append(" --- ");
        e.getBindingResult().getAllErrors().forEach(error -> errorMessageBuilder
                .append(error.getObjectName())
                .append(": ")
                .append(error.getDefaultMessage())
                .append("; "));

        ErrorInfo errorInfo = generateStandardErrorInfo(40040L, new String(errorMessageBuilder), e,
                request.getRequestURI());
        log.error("Incompatible parameters are passed: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
                                                                      HttpServletRequest request,
                                                                      Locale locale) {
        String errorMessagePart = messageSource.getMessage(METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION, null, locale);
        String errorMessage = e.getName() + errorMessagePart + Objects.requireNonNull(e.getRequiredType()).getName();
        ErrorInfo errorInfo = generateStandardErrorInfo(40050L, errorMessage, e, request.getRequestURI());
        log.error("Unexpected argument of a method is called: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorInfo> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
                                                                          HttpServletRequest request,
                                                                          Locale locale) {
        String errorMessagePart = messageSource.getMessage(MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION, null, locale);
        String errorMessage = e.getParameterName() + errorMessagePart;
        ErrorInfo errorInfo = generateStandardErrorInfo(40060L, errorMessage, e, request.getRequestURI());
        log.error("A request parameter is absent: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                         HttpServletRequest request,
                                                                         Locale locale) {
        String errorMessagePart = messageSource.getMessage(HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION, null, locale);
        String errorMessage = e.getMethod() + errorMessagePart;
        ErrorInfo errorInfo = generateStandardErrorInfo(40510L, errorMessage, e, request.getRequestURI());
        log.error("Incompatible http method is called: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOthers(RuntimeException e, HttpServletRequest request, Locale locale) {
        String errorMessage = messageSource.getMessage(OTHER_EXCEPTIONS, null, locale);
        ErrorInfo errorInfo = generateStandardErrorInfo(50099L, errorMessage, e, request.getRequestURI());
        log.error("An unexpected exception occurs: errorInfo → {}; exception → {}", errorInfo, e);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
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
