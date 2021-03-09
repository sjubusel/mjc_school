package com.epam.esm.aserver.exception;

import com.epam.esm.web.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class GeneralExceptionHandler {

    public static final String CONSTRAINT_VIOLATION_EXCEPTION = "constraint.violation.exception";
    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "method.argument.not.valid.exception";
    public static final String METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION = "method.argument.type.mismatch.exception";
    public static final String MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION
            = "missing.servlet.request.parameter.exception";
    public static final String HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION
            = "http.request.method.not.supported.exception";
    public static final String OTHER_EXCEPTIONS = "other.exceptions";
    private static final String AUTHENTICATION_EXCEPTION = "authentication.exception";

    private final MessageSource messageSource;

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

        ErrorInfo errorInfo = generateStandardErrorInfo(40030L, new String(errorMessageBuilder),
                request.getRequestURI());
        log.error("Incompatible parameters are passed: errorInfo → {}; exception → {}", errorInfo, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
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

        ErrorInfo errorInfo = generateStandardErrorInfo(40040L, new String(errorMessageBuilder),
                request.getRequestURI());
        log.error("Incompatible parameters are passed: errorInfo → {}; exception → {}", errorInfo, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
                                                                      HttpServletRequest request,
                                                                      Locale locale) {
        String errorMessagePart = messageSource.getMessage(METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION, null, locale);
        String errorMessage = e.getName() + errorMessagePart + Objects.requireNonNull(e.getRequiredType()).getName();
        ErrorInfo errorInfo = generateStandardErrorInfo(40050L, errorMessage, request.getRequestURI());
        log.error("Unexpected argument of a method is called: errorInfo → {}; exception → {}", errorInfo, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorInfo> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
                                                                          HttpServletRequest request,
                                                                          Locale locale) {
        String errorMessagePart = messageSource.getMessage(MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION, null, locale);
        String errorMessage = e.getParameterName() + errorMessagePart;
        ErrorInfo errorInfo = generateStandardErrorInfo(40060L, errorMessage, request.getRequestURI());
        log.error("A request parameter is absent: errorInfo → {}; exception → {}", errorInfo, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                         HttpServletRequest request,
                                                                         Locale locale) {
        String errorMessagePart = messageSource.getMessage(HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION, null, locale);
        String errorMessage = e.getMethod() + errorMessagePart;
        ErrorInfo errorInfo = generateStandardErrorInfo(40510L, errorMessage, request.getRequestURI());
        log.error("Incompatible http method is called: errorInfo → {}; exception → {}", errorInfo, e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorInfo);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    protected void handleAuthenticationException(HttpServletResponse response) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"oauth2/client\"");
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException e,
                                                                   HttpServletRequest request,
                                                                   Locale locale) {
        String errorMessage = messageSource.getMessage(AUTHENTICATION_EXCEPTION, null, locale);
        ErrorInfo errorInfo = generateStandardErrorInfo(40320L, errorMessage, request.getRequestURI());
        log.error("Access is forbidden → {}; exception → {}", errorInfo, e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorInfo);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOthers(RuntimeException e, HttpServletRequest request, Locale locale) {
        String errorMessage = messageSource.getMessage(OTHER_EXCEPTIONS, new Object[]{e.getClass().getName()}, locale);
        ErrorInfo errorInfo = generateStandardErrorInfo(50099L, errorMessage, request.getRequestURI());
        log.error("An unexpected exception occurs: errorInfo → {}; exception → {}", errorInfo, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInfo);
    }

    private ErrorInfo generateStandardErrorInfo(Long errorCode, String errorMessage, String uri) {
        return ErrorInfo.builder()
                .setErrorCode(errorCode)
                .setErrorMessage(errorMessage)
                .setUri(uri)
                .build();
    }
}
