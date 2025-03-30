package com.branas.clean_architecture.application.ports;

import com.branas.clean_architecture.driven.Ride;
import com.branas.clean_architecture.driver.RideInput;

import java.util.List;
import java.util.UUID;

public interface RideRepository {

    Ride getRideById(UUID rideId);

    List<Ride> getRidesByPassanger(UUID passengerId);

    UUID saveRide(RideInput rideInput);

}
