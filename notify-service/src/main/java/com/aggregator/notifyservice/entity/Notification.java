package com.aggregator.notifyservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long alertId;

    @Column(nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private BigDecimal thresholdRate;

    @Column(nullable = false)
    private BigDecimal currentRate;

    @Column(nullable = false)
    private BigDecimal difference;

    @Column(nullable = false)
    private boolean higher;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Instant triggeredAt;

    @Column(nullable = false)
    private Instant sentAt;

    public Notification(Long alertId, String currencyCode, BigDecimal thresholdRate,
                        BigDecimal currentRate, boolean higher, String type, Instant triggeredAt) {
        this.alertId = alertId;
        this.currencyCode = currencyCode;
        this.thresholdRate = thresholdRate;
        this.currentRate = currentRate;
        this.difference = currentRate.subtract(thresholdRate).abs();
        this.higher = higher;
        this.type = type;
        this.triggeredAt = triggeredAt;
        this.sentAt = Instant.now();
    }
}
