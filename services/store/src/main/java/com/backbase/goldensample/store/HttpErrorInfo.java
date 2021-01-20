package com.backbase.goldensample.store;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpErrorInfo {
    private final ZonedDateTime timestamp;
    private final String        path;
    private final HttpStatus    httpStatus;
    private final String        message;

    public HttpErrorInfo() {
        timestamp = null;
        httpStatus = null;
        path = null;
        message = null;
    }

    @Builder
    public HttpErrorInfo(final HttpStatus httpStatus, final String path, final String message) {
        timestamp = ZonedDateTime.now();
        this.httpStatus = httpStatus;
        this.path = path;
        this.message = message;
    }
}
