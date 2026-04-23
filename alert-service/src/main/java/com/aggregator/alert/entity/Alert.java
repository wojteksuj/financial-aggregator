package com.aggregator.alert.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="alerts")
@Getter
@Setter
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private BigDecimal thresholdRate;

    @Column(nullable = false)
    private boolean higher;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private Instant createdAt;

    protected Alert(){}

    public Alert(String currencyCode, BigDecimal thresholdRate, boolean higher) {
        this.currencyCode = currencyCode;
        this.thresholdRate = thresholdRate;
        this.higher = higher;
        this.active = true;
        this.createdAt = Instant.now();
    }
}
