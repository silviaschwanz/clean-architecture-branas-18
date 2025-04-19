package com.branas.clean_architecture.application.dto;

public record RideOutput(
        String rideId,
        String passengerId,
        String driverId,
        double fromLatitude,
        double fromLongitude,
        double toLatitude,
        double toLongitude,
        String status
) {
}
