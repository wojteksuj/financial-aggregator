package com.aggregator.market.service.component;

import com.aggregator.market.service.RateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class RateSchedulerTest {

    @Mock
    private RateService rateService;

    @InjectMocks
    private RateScheduler rateScheduler;

    @Test
    void scheduleFetch_delegatesToRateService() {
        rateScheduler.scheduleFetch();

        verify(rateService, times(1)).fetchAndSave();
    }
}
