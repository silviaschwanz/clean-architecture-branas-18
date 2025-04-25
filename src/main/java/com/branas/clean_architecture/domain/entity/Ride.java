package com.branas.clean_architecture.domain.entity;

import com.branas.clean_architecture.domain.vo.Coordinates;
import com.branas.clean_architecture.domain.vo.RideStatus;
import com.branas.clean_architecture.domain.vo.RideStatusFactory;
import com.branas.clean_architecture.domain.vo.RideStatusRequested;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class Ride {

    private UUID rideId;
    private UUID passengerId;
    private UUID driverId;
    private RideStatus status;
    private double fare;
    private Coordinates from;
    private Coordinates to;
    private double distance;
    private LocalDateTime date;

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
        return new Ride(
                UUID.randomUUID(),
                UUID.fromString(passengerId),
                null,
                new RideStatusRequested(),
                0,
                new Coordinates(fromLatitude, fromLongitude),
                new Coordinates(toLatitude, toLongitude),
                0,
                LocalDateTime.now(clock)
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
                RideStatusFactory.create(status),
                fare,
                new Coordinates(fromLatitude, fromLongitude),
                new Coordinates(toLatitude, toLongitude),
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
        return status.getValue();
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

    private void changeStatus(RideStatus status) {
        this.status = status;
    }

    public void accept(String driverId) {
        changeStatus(this.status.accept());
        this.driverId = UUID.fromString(driverId);
    }

    public void start() {
        changeStatus(this.status.start());
    }

}
