package com.backbase.goldensample.store;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class HttpErrorTest {

    @Test
    void testEmptyConstructor() {
        HttpErrorInfo httpErrorInfoEmpty = new HttpErrorInfo();

        assertAll(
            () -> assertEquals(null, httpErrorInfoEmpty.getTimestamp()),
            () -> assertEquals(null, httpErrorInfoEmpty.getMessage()),
            () -> assertEquals(null, httpErrorInfoEmpty.getPath()));
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
