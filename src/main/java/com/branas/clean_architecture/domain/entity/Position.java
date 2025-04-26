package com.branas.clean_architecture.domain.entity;

import com.branas.clean_architecture.domain.vo.Coordinates;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class Position {

    private UUID positionId;
    private UUID rideId;
    private Coordinates coordinates;
    private LocalDateTime date;

    private Position(UUID positionId, UUID rideId, Coordinates coordinates, LocalDateTime date) {
        this.positionId = positionId;
        this.rideId = rideId;
        this.coordinates = coordinates;
        this.date = date;
    }

    public static Position create(String rideId, double latitude, double longitude, Clock clock) {
        return new Position(
                UUID.randomUUID(),
                UUID.fromString(rideId),
                new Coordinates(latitude, longitude),
                LocalDateTime.now(clock)
        );
    }

    public static Position restore(UUID positionId, UUID rideId, double latitude, double longitude, LocalDateTime date) {
        return new Position(
                positionId,
                rideId,
                new Coordinates(latitude, longitude),
                date
        );
    }

    public String getPositionId() {
        return positionId.toString();
    }

    public String getRideId() {
        return rideId.toString();
    }

    public double getLatitude() {
        return coordinates.latitude();
    }

    public double getLongitude() {
        return coordinates.longitude();
    }

    public LocalDateTime getDate() {
        return date;
    }

}
