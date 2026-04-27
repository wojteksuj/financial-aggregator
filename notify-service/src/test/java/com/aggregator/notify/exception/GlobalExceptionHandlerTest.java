package com.aggregator.notify.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleCurrencyCodeNotFoundException_returnsNotFound() {
        CurrencyCodeNotFoundException ex = new CurrencyCodeNotFoundException("XYZ");
        ProblemDetail detail = handler.handleCurrencyCodeNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND.value(), detail.getStatus());
        assertEquals("no notifications found for currency code: XYZ", detail.getDetail());
    }

    @Test
    void handleMethodArgumentTypeMismatchException_returnsBadRequest() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getMessage()).thenReturn("Failed to convert value");
        ProblemDetail detail = handler.handleMethodArgumentTypeMismatchException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), detail.getStatus());
        assertEquals("Failed to convert value", detail.getDetail());
    }
}
