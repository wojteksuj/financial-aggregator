package com.aggregator.notify.repository;

import com.aggregator.notify.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByCurrencyCode(String currencyCode);
}
