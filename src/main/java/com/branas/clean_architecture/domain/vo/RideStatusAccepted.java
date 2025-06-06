package com.branas.clean_architecture.domain.vo;

import com.branas.clean_architecture.domain.Status;

public class RideStatusAccepted implements RideStatus{

    private String value;

    public RideStatusAccepted() {
        this.value = Status.ACCEPTED.toString();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public RideStatus accept() {
        throw new RuntimeException("Invalid status");
    }

    @Override
    public RideStatus start() {
        return new RideStatusInProgress();
    }

    @Override
    public RideStatus finish() {
        throw new RuntimeException("Invalid status");
    }
}
