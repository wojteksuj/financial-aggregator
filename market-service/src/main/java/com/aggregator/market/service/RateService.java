package com.aggregator.market.service;

import com.aggregator.market.dto.RateResponseDto;
import com.aggregator.market.entity.ExchangeRate;
import com.aggregator.market.repository.ExchangeRateRepository;
import com.aggregator.market.service.component.ExchangeRateClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

@Service
public class RateService {
    private final ExchangeRateRepository rateRepository;
    private final ExchangeRateClient exchangeRateClient;

    public RateService(ExchangeRateRepository rateRepository, ExchangeRateClient exchangeRateClient) {
        this.rateRepository = rateRepository;
        this.exchangeRateClient = exchangeRateClient;
    }

    public List<RateResponseDto> getRates(String currencyCode, LocalDate date) {
        Instant startDate = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endDate = date.atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant();

        return rateRepository.findByCurrencyCodeAndFetchedAtBetween(currencyCode, startDate, endDate)
                .stream()
                .map(rate -> new RateResponseDto(rate.getCurrencyCode(), rate.getBaseCurrency(), rate.getFetchedAt(), rate.getRate()))
                .toList();
    }

    public void fetchAndSave() {
        Set<String> wantedCurrencies = Set.of("EUR", "USD", "GBP", "CHF", "CZK", "JPY");
        var rates = exchangeRateClient.fetchRates();

        List<ExchangeRate> savedRates = rates.conversionRates().entrySet().stream()
                .filter(entry -> wantedCurrencies.contains(entry.getKey()))
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
