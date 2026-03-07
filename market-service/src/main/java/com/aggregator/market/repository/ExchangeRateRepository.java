package com.aggregator.market.repository;

import com.aggregator.market.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByCurrencyCodeAndFetchedAtBetween(String currencyCode, Instant fetchedAtAfter, Instant fetchedAtBefore);
}
