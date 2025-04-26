package com.branas.clean_architecture.application.dto;

public record InputUpdatePosition(
        String rideId,
        double latitude,
        double longitude
) {
}
