package com.aggregator.market.service.component;

import com.aggregator.market.service.RateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateScheduler {
    private final RateService rateService;

    public RateScheduler(RateService rateService) {
        this.rateService = rateService;
    }

    @Scheduled(fixedDelay = 300_000)
    public void scheduleFetch(){
        rateService.fetchAndSave();
    }
}
