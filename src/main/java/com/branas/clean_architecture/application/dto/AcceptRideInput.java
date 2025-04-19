package com.branas.clean_architecture.application.dto;

public record AcceptRideInput(
        String rideId,
        String driverId
) {
}
