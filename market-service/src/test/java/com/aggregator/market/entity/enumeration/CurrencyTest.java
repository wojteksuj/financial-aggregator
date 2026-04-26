package com.aggregator.market.entity.enumeration;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyTest {

    @Test
    void getAllCodes_containsAllExpectedCurrencies() {
        List<String> codes = Currency.getAllCodes();

        assertTrue(codes.contains("EUR"));
        assertTrue(codes.contains("USD"));
        assertTrue(codes.contains("GBP"));
        assertTrue(codes.contains("CHF"));
        assertTrue(codes.contains("CZK"));
        assertTrue(codes.contains("JPY"));
    }

    @Test
    void getAllCodes_sizeMatchesEnumValues() {
        assertEquals(Currency.values().length, Currency.getAllCodes().size());
    }

    @Test
    void getAllCodes_doesNotContainUnknownCode() {
        assertFalse(Currency.getAllCodes().contains("XYZ"));
    }

    @Test
    void getCurrencyCode_returnsCorrectCode() {
        assertEquals("EUR", Currency.EURO.getCurrencyCode());
        assertEquals("USD", Currency.UNITED_STATES_DOLLAR.getCurrencyCode());
        assertEquals("JPY", Currency.JAPANESE_YEN.getCurrencyCode());
    }
}
