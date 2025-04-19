package com.branas.clean_architecture.domain.entity;

import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.vo.Coordinates;

import java.time.LocalDateTime;
import java.util.Objects;
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
        this.fare = 0;
        this.from = new Coordinates(fromLatitude, fromLongitude);
        this.to = new Coordinates(toLatitude, toLongitude);
        this.distance = 0;
        this.date = date;
    }

    private Ride(
            UUID rideId,
            UUID passengerId,
            UUID driverId,
            String status,
            double fare,
            double fromLatitude,
            double fromLongitude,
            double toLatitude,
            double toLongitude,
            double distance,
            LocalDateTime date
    ) {
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.driverId = driverId;
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
            UUID rideId,
            UUID passengerId,
            UUID driverId,
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
                driverId,
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

    public String getDriverId() {
        if(driverId == null) return  "";
        return driverId.toString();
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

    public double getFare() {
        return fare;
    }

    public double getDistance() {
        return distance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void accept(String driverId) {
        if(!Objects.equals(this.getStatus(), Status.REQUESTED.toString())) throw new RuntimeException("Invalid status");
        this.status = Status.ACCEPTED.toString();
        this.driverId = UUID.fromString(driverId);
    }

    public void start() {
        if(!Objects.equals(this.getStatus(), Status.ACCEPTED.toString())) throw new RuntimeException("Invalid status");
        this.status = Status.IN_PROGRESS.toString();
    }

}
