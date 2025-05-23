package com.branas.clean_architecture.domain.entity;

import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.service.DistanceCalculator;
import com.branas.clean_architecture.domain.vo.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Ride {

    private final UUID rideId;
    private final UUID passengerId;
    private UUID driverId;
    private RideStatus status;
    private double fare;
    private final Coordinates from;
    private final Coordinates to;
    private double distance;
    private final LocalDateTime date;

    private Ride(
            UUID rideId,
            UUID passengerId,
            UUID driverId,
            RideStatus status,
            double fare,
            Coordinates from,
            Coordinates to,
            double distance,
            LocalDateTime date
    ) {
        this.rideId     = rideId;
        this.passengerId= passengerId;
        this.driverId   = driverId;
        this.status     = status;
        this.fare       = fare;
        this.from       = from;
        this.to         = to;
        this.distance   = distance;
        this.date       = date;
    }

    public static Ride create(
            String passengerId,
            double fromLatitude,
            double fromLongitude,
            double toLatitude,
            double toLongitude,
            Clock clock
    ) {
        var rideId = UUID.randomUUID();
        var passangerId = UUID.fromString(passengerId);
        var distance = 0;
        var fare = 0;
        var status = new RideStatusRequested();
        var from = new Coordinates(fromLatitude, fromLongitude);
        var to = new Coordinates(toLatitude, toLongitude);
        var date = LocalDateTime.now(clock);
        return new Ride(rideId, passangerId, null, status, fare, from, to, distance, date);
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
        var statusRestore = RideStatusFactory.create(status);
        var from = new Coordinates(fromLatitude, fromLongitude);
        var to = new Coordinates(toLatitude, toLongitude);
        return new Ride( rideId, passengerId, driverId, statusRestore, fare, from, to, distance, date);
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
        return status.getValue();
    }

    public double getFare() {
        return fare;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getDistance() {
        return distance;
    }

    public void accept(String driverId) {
        changeStatus(status.accept());
        this.driverId = UUID.fromString(driverId);
    }

    public void start() {
        changeStatus(status.start());
    }

    public void validateStatusForUpdatePosition() {
        if(!(Objects.equals(status.getValue(), Status.IN_PROGRESS.toString()))) {
            throw new RuntimeException("Invalid status");
        }
    }

    public void finish(List<Position> positions) {
        distance = DistanceCalculator.calculateDistance(positions);
        fare = distance * 2.0;
        changeStatus(status.finish());
    }

    private void changeStatus(RideStatus status) {
        this.status = status;
    }



}
