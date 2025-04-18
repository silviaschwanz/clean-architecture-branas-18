package com.branas.clean_architecture.domain.ride;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ride {

    private UUID rideId;
    private UUID passengerId;
    private UUID driverId;
    private String status;
    private double fare;
    private Coordinates from;
    private Coordinates to;
    private double distance;
    private LocalDateTime date;

    private Ride(
            String rideId,
            String passengerId,
            String status,
            double fromLatitude,
            double fromLongitude,
            double toLatitude,
            double toLongitude,
            LocalDateTime date
    ) {
        this.rideId = UUID.fromString(rideId);
        this.passengerId = UUID.fromString(passengerId);
        this.status = status;
        this.from = new Coordinates(fromLatitude, fromLongitude);
        this.to = new Coordinates(toLatitude, toLongitude);
        this.date = date;
    }

    private Ride(
            String rideId,
            String passengerId,
            String status,
            double fare,
            double fromLatitude,
            double fromLongitude,
            double toLatitude,
            double toLongitude,
            double distance,
            LocalDateTime date
    ) {
        this.rideId = UUID.fromString(rideId);
        this.passengerId = UUID.fromString(passengerId);
        this.status = status;
        this.fare = fare;
        this.from = new Coordinates(fromLatitude, fromLongitude);
        this.to = new Coordinates(toLatitude, toLongitude);
        this.distance = distance;
        this.date = date;

    }

    public static Ride create(
            String passengerId,
            double fromLatitude,
            double fromLongitude,
            double toLatitude,
            double toLongitude
    ) {
        return new Ride(
                UUID.randomUUID().toString(),
                passengerId,
                Status.REQUESTED.toString(),
                fromLatitude,
                fromLongitude,
                toLatitude,
                toLongitude,
                LocalDateTime.now()
        );
    }

    public static Ride restore(
            String rideId,
            String passengerId,
            String status,
            double fare,
            double fromLatitude,
            double fromLongitude,
            double toLatitude,
            double toLongitude,
            double distance,
            LocalDateTime date
    ) {
        return new Ride(
                rideId,
                passengerId,
                status,
                fare,
                fromLatitude,
                fromLongitude,
                toLatitude,
                toLongitude,
                distance,
                date
        );
    }

    public String getRideId() {
        return rideId.toString();
    }

    public String getPassengerId() {
        return passengerId.toString();
    }

    public double getFromLatitude() {
        return from.latitude();
    }

    public double getFromLongitude() {
        return from.longitude();
    }

    public double getToLatitude() {
        return to.latitude();
    }

    public double getToLongitude() {
        return to.longitude();
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }

}
