package com.branas.clean_architecture.driven;

import java.time.LocalDateTime;

public record Ride(
        String rideId,
        String passengerId,
        String driverId,
        String status,
        double fare,
        double fromLat,
        double fromLong,
        double toLat,
        double toLong,
        double distance,
        LocalDateTime date
) {



}
