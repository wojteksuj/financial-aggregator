package com.aggregator.notify.service;

import com.aggregator.notify.dto.TriggeredAlertEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.notification.recipient-email}")
    private String recipientEmail;

    @Value("${app.notification.sender-email}")
    private String senderEmail;

    public void sendAlertEmail(TriggeredAlertEventDto event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(recipientEmail);
        message.setSubject(buildSubject(event));
        message.setText(buildBody(event));
        mailSender.send(message);
    }

    private String buildSubject(TriggeredAlertEventDto event) {
        String direction = event.higher() ? "above" : "below";
        return "[Alert] %s is %s threshold %s".formatted(
                event.currencyCode(),
                direction,
                event.thresholdRate()
        );
    }

    private String buildBody(TriggeredAlertEventDto event) {
        String direction = event.higher() ? "above" : "below";
        return """
                Alert triggered!
                
                Currency:      %s
                Threshold:     %s
                Current rate:  %s
                Difference:    %s
                Direction:     %s threshold
                Triggered at:  %s
                """.formatted(
                event.currencyCode(),
                event.thresholdRate(),
                event.currentRate(),
                event.currentRate().subtract(event.thresholdRate()).abs(),
                direction,
                event.triggeredAt()
        );
    }
}
