package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.dto.InputStartRide;
import com.branas.clean_architecture.application.dto.OutputStartRide;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.entity.Ride;
import org.springframework.stereotype.Service;

@Service
public class StartRide {

    private final RideRepository rideRepository;

    public StartRide(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public OutputStartRide execute(InputStartRide inputStartRide) {
        Ride ride = rideRepository.getRideById(inputStartRide.rideId());
        ride.start();
        rideRepository.update(ride);
        return new OutputStartRide(ride.getRideId());
    }

}
