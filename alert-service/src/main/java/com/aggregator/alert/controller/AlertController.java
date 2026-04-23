package com.aggregator.alert.controller;

import com.aggregator.alert.dto.request.NewAlertDto;
import com.aggregator.alert.dto.response.AlertResponseDto;
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
    public ResponseEntity<AlertResponseDto> createAlert(@Valid @RequestBody NewAlertDto newAlertDto){
        AlertResponseDto response = alertService.createAlert(newAlertDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
