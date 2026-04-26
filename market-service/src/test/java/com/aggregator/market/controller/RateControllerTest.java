package com.aggregator.market.controller;

import com.aggregator.market.dto.RateResponseDto;
import com.aggregator.market.exception.GlobalExceptionHandler;
import com.aggregator.market.exception.InvalidRequestException;
import com.aggregator.market.service.RateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {RateController.class, GlobalExceptionHandler.class})
public class RateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RateService rateService;

    @Test
    void getRates_returnsOkWithRates() throws Exception {
        RateResponseDto dto = new RateResponseDto("EUR", "PLN", Instant.parse("2026-04-26T10:00:00Z"), new BigDecimal("4.25"));
        when(rateService.getRates(eq("EUR"), any(LocalDate.class))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/rates/EUR").param("date", "2026-04-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currencyCode").value("EUR"))
                .andExpect(jsonPath("$[0].baseCurrency").value("PLN"))
                .andExpect(jsonPath("$[0].rate").value(4.25));
    }

    @Test
    void getRates_returnsEmptyListWhenNoRatesFound() throws Exception {
        when(rateService.getRates(eq("USD"), any(LocalDate.class))).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/rates/USD").param("date", "2026-04-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getRates_returnsBadRequestOnFutureDate() throws Exception {
        when(rateService.getRates(eq("EUR"), any(LocalDate.class)))
                .thenThrow(new InvalidRequestException("provided date is in the future"));

        mockMvc.perform(get("/api/v1/rates/EUR").param("date", "2099-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRates_returnsBadRequestOnInvalidDateFormat() throws Exception {
        mockMvc.perform(get("/api/v1/rates/EUR").param("date", "not-a-date"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRates_returnsMissingParamWhenDateAbsent() throws Exception {
        mockMvc.perform(get("/api/v1/rates/EUR"))
                .andExpect(status().is4xxClientError());
    }
}
