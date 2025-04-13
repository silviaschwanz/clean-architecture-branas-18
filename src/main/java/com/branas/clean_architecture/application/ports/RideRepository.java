package com.branas.clean_architecture.application.ports;

import com.branas.clean_architecture.domain.ride.Ride;

import java.util.List;

public interface RideRepository {

    Ride getRideById(String rideId);

    List<Ride> getRidesByPassanger(String passengerId);

    Ride saveRide(Ride ride);

}
