package com.branas.clean_architecture.domain.vo;

import com.branas.clean_architecture.domain.Status;

public class RideStatusRequested implements RideStatus{

    private String value;

    public RideStatusRequested() {
        this.value = Status.REQUESTED.toString();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public RideStatus accept() {
        return new RideStatusAccepted();
    }

    @Override
    public RideStatus start() {
        throw new RuntimeException("Invalid status");
    }

    @Override
    public RideStatus finish() {
        throw new RuntimeException("Invalid status");
    }
}
