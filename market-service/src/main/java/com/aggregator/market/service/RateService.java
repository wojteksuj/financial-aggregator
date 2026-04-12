package com.aggregator.market.service;

import com.aggregator.market.dto.RateResponseDto;
import com.aggregator.market.entity.ExchangeRate;
import com.aggregator.market.entity.enumeration.Currency;
import com.aggregator.market.exception.ExchangeRateApiException;
import com.aggregator.market.exception.InvalidRequestException;
import com.aggregator.market.exception.RatesNotFoundException;
import com.aggregator.market.repository.ExchangeRateRepository;
import com.aggregator.market.service.component.ExchangeRateClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class RateService {
    private final ExchangeRateRepository rateRepository;
    private final ExchangeRateClient exchangeRateClient;

    public RateService(ExchangeRateRepository rateRepository, ExchangeRateClient exchangeRateClient) {
        this.rateRepository = rateRepository;
        this.exchangeRateClient = exchangeRateClient;
    }

    public List<RateResponseDto> getRates(String currencyCode, LocalDate date) {
        if (date.isAfter(LocalDate.now())) throw new InvalidRequestException("provided date is in the future");

        Instant startDate = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endDate = date.atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant();

        return rateRepository.findByCurrencyCodeAndFetchedAtBetween(currencyCode, startDate, endDate)
                .stream()
                .map(rate -> new RateResponseDto(rate.getCurrencyCode(), rate.getBaseCurrency(), rate.getFetchedAt(), rate.getRate()))
                .toList();
    }

    public void fetchAndSave() {
        var rates = exchangeRateClient.fetchRates();
        if (!"success".equals(rates.result()))
            throw new ExchangeRateApiException("external api error: " + rates.result());
        if (rates.conversionRates().isEmpty()) throw new RatesNotFoundException("rates not found for provided base code");

        List<ExchangeRate> savedRates = rates.conversionRates().entrySet().stream()
                .filter(entry -> Currency.getAllCodes().contains(entry.getKey()))
                .map(entry -> new ExchangeRate(
                        entry.getKey(),
                        rates.baseCode(),
                        entry.getValue(),
                        Instant.now()
                ))
                .toList();

        rateRepository.saveAll(savedRates);
    }
}
