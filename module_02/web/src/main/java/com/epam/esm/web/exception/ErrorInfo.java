package com.epam.esm.web.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class ErrorInfo {
    private Long errorCode;
    private String errorMessage;
    private String exceptionName;
    private String uri;
}
