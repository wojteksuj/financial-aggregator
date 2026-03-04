package com.aggregator.market.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "exchange_rates")
@Getter
@Setter
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String currencyCode;
    @Column(nullable = false)
    private String baseCurrency;
    @Column(nullable = false)
    private BigDecimal rate;
    @Column(nullable = false)
    private Instant fetchedAt;

    protected ExchangeRate() {}
}
