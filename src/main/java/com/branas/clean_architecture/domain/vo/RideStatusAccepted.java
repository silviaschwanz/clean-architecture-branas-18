package com.branas.clean_architecture.domain.vo;

import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.entity.Ride;

public class RideStatusAccepted implements RideStatus{

    private String value;
    private Ride ride;


    public RideStatusAccepted(Ride ride) {
        this.ride = ride;
        this.value = Status.ACCEPTED.toString();
    }

    public String getValue() {
        return value;
    }

    @Override
    public void request() {
        throw new RuntimeException("Invalid status");
    }

    @Override
    public void accept() {
        throw new RuntimeException("Invalid status");
    }

    @Override
    public void start() {
        this.ride.setStatus(new RideStatusInProgress(this.ride));
    }
}
