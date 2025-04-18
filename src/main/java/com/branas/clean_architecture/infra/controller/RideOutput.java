package com.branas.clean_architecture.infra.controller;

public record RideOutput(
        String rideId,
        String passengerId,
        double fromLatitude,
        double fromLongitude,
        double toLatitude,
        double toLongitude,
        String status
) {
}
