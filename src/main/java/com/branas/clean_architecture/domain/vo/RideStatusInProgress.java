package com.branas.clean_architecture.domain.vo;

import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.entity.Ride;

public class RideStatusInProgress implements RideStatus{

    private String value;
    private Ride ride;

    public RideStatusInProgress(Ride ride) {
        this.ride = ride;
        this.value = Status.IN_PROGRESS.toString();
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
        throw new RuntimeException("Invalid status");
    }

    @Override
    public void start() {
        throw new RuntimeException("Invalid status");
    }
}
