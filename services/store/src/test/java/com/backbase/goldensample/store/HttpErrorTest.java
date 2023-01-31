package com.backbase.goldensample.store;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class HttpErrorTest {

    @Test
    void testEmptyConstructor() {
        HttpErrorInfo httpErrorInfoEmpty = new HttpErrorInfo();

        assertAll(
            () -> assertNull(httpErrorInfoEmpty.getTimestamp()),
            () -> assertNull(httpErrorInfoEmpty.getMessage()),
            () -> assertNull(httpErrorInfoEmpty.getPath()));
    }

    @Test
    void testConstructor() {
        HttpErrorInfo httpErrorInfo = new HttpErrorInfo(HttpStatus.NOT_FOUND, "path","message");

        assertAll(
            () -> assertNotNull(httpErrorInfo.getTimestamp()),
            () -> assertEquals(HttpStatus.NOT_FOUND.value(), httpErrorInfo.getStatus()),
            () -> assertEquals("message", httpErrorInfo.getMessage()),
            () -> assertEquals("path", httpErrorInfo.getPath()));
    }

}
