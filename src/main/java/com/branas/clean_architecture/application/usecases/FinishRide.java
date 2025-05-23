package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.dto.InputFinishRide;
import com.branas.clean_architecture.application.dto.OutputRide;
import com.branas.clean_architecture.application.ports.PositionRepository;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.entity.Position;
import com.branas.clean_architecture.domain.entity.Ride;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

@Service
public class FinishRide {

    private final RideRepository rideRepository;
    private final PositionRepository positionRepository;

    public FinishRide(RideRepository rideRepository, PositionRepository positionRepository) {
        this.rideRepository = rideRepository;
        this.positionRepository = positionRepository;
    }

    public OutputRide execute(InputFinishRide inputFinishRide) {
        Ride ride = rideRepository.getRideById(inputFinishRide.rideId());
        List<Position> positions = positionRepository.getPositionsByRideId(ride.getRideId());
        ride.finish(positions);
        rideRepository.update(ride);
        return new OutputRide(
                ride.getRideId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getFromLatitude(),
                ride.getFromLongitude(),
                ride.getToLatitude(),
                ride.getFromLongitude(),
                ride.getStatus(),
                positions,
                ride.getDistance(),
                ride.getFare()
        );
    }

}
