package com.aggregator.market.service;

import com.aggregator.market.dto.RateResponseDto;
import com.aggregator.market.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class RateService {
    private final ExchangeRateRepository rateRepository;

    public RateService(ExchangeRateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public List<RateResponseDto> getRates(String currencyCode, LocalDate date) {
        Instant startDate = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endDate = date.atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant();

        return rateRepository.findByCurrencyCodeAndFetchedAtBetween(currencyCode, startDate, endDate)
                .stream()
                .map(rate -> new RateResponseDto(rate.getCurrencyCode(), rate.getBaseCurrency(), rate.getFetchedAt(), rate.getRate()))
                .toList();
    }
}
