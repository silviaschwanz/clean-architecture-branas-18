package com.branas.clean_architecture.infra.controller;

import com.branas.clean_architecture.application.usecases.RequestRide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
public class RideController {

    @Autowired
    RequestRide requestRide;

    @PostMapping()
    public ResponseEntity<?> getRide(@RequestBody RideInput input) {
        try {
            var rideOutput = requestRide.execute(input);
            return ResponseEntity.ok().body(rideOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorOutput(e.getMessage()));
        }
    }

}


