package com.branas.clean_architecture.domain.vo;

import com.branas.clean_architecture.domain.Status;

public class RideStatusFactory {

    public static RideStatus create(String status) {
        if(status.equals(Status.REQUESTED.toString())) return new RideStatusRequested();
        if(status.equals(Status.ACCEPTED.toString())) return new RideStatusAccepted();
        if(status.equals(Status.IN_PROGRESS.toString())) return new RideStatusInProgress();
        throw new RuntimeException("Invalid Status");
    }
}
