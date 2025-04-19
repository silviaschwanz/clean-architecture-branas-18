package com.branas.clean_architecture.domain.vo;

import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.entity.Ride;

public class RideStatusRequested implements RideStatus{

    private String value;
    private Ride ride;

    public RideStatusRequested(Ride ride) {
        this.ride = ride;
        this.value = Status.REQUESTED.toString();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void request() {
        throw new RuntimeException("Invalid status");
    }

    @Override
    public void accept() {
        ride.setStatus(new RideStatusAccepted(this.ride));
    }

    @Override
    public void start() {
        throw new RuntimeException("Invalid status");
    }
}
