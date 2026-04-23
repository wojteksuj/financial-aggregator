package com.aggregator.alert.controller;

import com.aggregator.alert.dto.request.NewAlertDto;
import com.aggregator.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/v1/alerts"))
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public void createAlert(@RequestBody NewAlertDto newAlertDto){

    }

}
