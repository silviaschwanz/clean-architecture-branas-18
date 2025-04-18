package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.ride.Ride;
import com.branas.clean_architecture.infra.controller.RideOutput;
import org.springframework.stereotype.Service;

@Service
public class GetRide {

    private final RideRepository rideRepository;

    public GetRide(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public RideOutput execute(String rideId) {
        Ride ride = rideRepository.getRideById(rideId);
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
