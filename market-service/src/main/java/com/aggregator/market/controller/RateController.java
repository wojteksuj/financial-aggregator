package com.aggregator.market.controller;

import com.aggregator.market.dto.RateResponseDto;
import com.aggregator.market.entity.ExchangeRate;
import com.aggregator.market.service.RateService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rates")
public class RateController {
    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/{currencyCode}")
    public List<RateResponseDto> getRates(
            @PathVariable String currencyCode,
            @RequestParam LocalDate date
    ) {
        return rateService.getRates(currencyCode, date);
    }
}
