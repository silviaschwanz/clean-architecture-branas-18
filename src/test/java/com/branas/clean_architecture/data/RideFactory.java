package com.branas.clean_architecture.data;

import com.branas.clean_architecture.domain.entity.Ride;

import java.time.Clock;

public class RideFactory {

    public static Ride createRide(String accountPassanger, Clock fixedClock) {
        return Ride.create(
                accountPassanger,
                -27.584905257808835,
                -48.545022195325124,
                -27.496887588317275,
                -48.522234807851476,
                fixedClock
        );
    }
}
