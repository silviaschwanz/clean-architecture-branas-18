package com.branas.clean_architecture.application.ports;

import com.branas.clean_architecture.domain.entity.Ride;

import java.util.List;

public interface RideRepository {

    Ride getRideById(String rideId);

    List<Ride> getRidesByPassanger(String passengerId);

    Ride saveRide(Ride ride);

    void update(Ride ride);

}
