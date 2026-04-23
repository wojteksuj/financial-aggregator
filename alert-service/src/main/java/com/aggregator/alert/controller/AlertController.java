package com.aggregator.alert.controller;

import com.aggregator.alert.dto.request.CreateAlertRequest;
import com.aggregator.alert.dto.response.CreateAlertResponse;
import com.aggregator.alert.service.AlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/api/v1/alerts"))
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<CreateAlertResponse> createAlert(@Valid @RequestBody CreateAlertRequest createAlertRequest){
        CreateAlertResponse response = alertService.createAlert(createAlertRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
