package com.branas.clean_architecture.domain.vo;

import com.branas.clean_architecture.domain.Status;

public class RideStatusInProgress implements RideStatus{

    private String value;

    public RideStatusInProgress() {
        this.value = Status.IN_PROGRESS.toString();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public RideStatus request() {
        throw new RuntimeException("Invalid status");
    }

    @Override
    public RideStatus accept() {
        throw new RuntimeException("Invalid status");
    }

    @Override
    public RideStatus start() {
        throw new RuntimeException("Invalid status");
    }
}
