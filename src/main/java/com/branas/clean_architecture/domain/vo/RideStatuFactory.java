package com.branas.clean_architecture.domain.vo;

import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.entity.Ride;

public class RideStatuFactory {

    public static RideStatus create(String status, Ride ride) {
        if(status.equals(Status.REQUESTED.toString())) return new RideStatusRequested(ride);
        if(status.equals(Status.ACCEPTED.toString())) return new RideStatusAccepted(ride);
        if(status.equals(Status.IN_PROGRESS.toString())) return new RideStatusInProgress(ride);
        throw new RuntimeException("Invalid Status");
    }
}
