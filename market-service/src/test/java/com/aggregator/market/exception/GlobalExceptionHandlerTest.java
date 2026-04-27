package com.aggregator.market.exception;

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
    void handleExchangeRateApiException_returnsBadGateway() {
        ExchangeRateApiException ex = new ExchangeRateApiException("upstream failure");
        ProblemDetail detail = handler.handleExchangeRateApiException(ex);

        assertEquals(HttpStatus.BAD_GATEWAY.value(), detail.getStatus());
    }

    @Test
    void handleInvalidRequestException_returnsBadRequest() {
        InvalidRequestException ex = new InvalidRequestException("provided date is in the future");
        ProblemDetail detail = handler.handleInvalidRequestException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), detail.getStatus());
        assertEquals("provided date is in the future", detail.getDetail());
    }

    @Test
    void handleRatesNotFoundException_returnsNotFound() {
        RatesNotFoundException ex = new RatesNotFoundException();
        ProblemDetail detail = handler.handleRatesNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND.value(), detail.getStatus());
        assertEquals("rates not found for provided base code", detail.getDetail());
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
