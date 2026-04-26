package com.aggregator.notifyservice.repository;

import com.aggregator.notifyservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByCurrencyCode(String currencyCode);
}
