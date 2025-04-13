package com.branas.clean_architecture.application;

import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.ride.Ride;
import com.branas.clean_architecture.driver.RideOutput;
import org.springframework.stereotype.Service;

@Service
public class GetRide {

    private RideRepository rideDAO;

    public GetRide(RideRepository rideDAO) {
        this.rideDAO = rideDAO;
    }

    public RideOutput execute(String rideId) {
        Ride ride = rideDAO.getRideById(rideId);
        return new RideOutput(
                ride.getRideId(),
                ride.getPassengerId(),
                ride.getFromLatitude(),
                ride.getFromLongitude(),
                ride.getToLatitude(),
                ride.getToLongitude(),
                ride.getStatus()
        );
    }

}
