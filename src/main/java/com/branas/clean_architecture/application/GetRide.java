package com.branas.clean_architecture.application;

import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.driven.Ride;
import com.branas.clean_architecture.driver.RideOutput;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetRide {

    private RideRepository rideDAO;

    public GetRide(RideRepository rideDAO) {
        this.rideDAO = rideDAO;
    }

    public RideOutput execute(UUID rideId) {
        Ride ride = rideDAO.getRideById(rideId);
        return new RideOutput(
                ride.rideId(),
                ride.passengerId(),
                ride.fromLat(),
                ride.fromLong(),
                ride.toLat(),
                ride.toLong(),
                ride.status()
        );
    }

}
