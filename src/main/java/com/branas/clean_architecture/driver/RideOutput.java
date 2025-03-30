package com.branas.clean_architecture.driver;

public record RideOutput(
        String rideId,
        String passengerId,
        double fromLat,
        double fromLong,
        double toLat,
        double toLong,
        String status
) {
}
