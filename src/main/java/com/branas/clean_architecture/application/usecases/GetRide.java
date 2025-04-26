package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.ports.PositionRepository;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.entity.Position;
import com.branas.clean_architecture.domain.entity.Ride;
import com.branas.clean_architecture.application.dto.RideOutput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetRide {

    private final RideRepository rideRepository;

    private final PositionRepository positionRepository;

    public GetRide(RideRepository rideRepository, PositionRepository positionRepository) {
        this.rideRepository = rideRepository;
        this.positionRepository = positionRepository;
    }

    public RideOutput execute(String rideId) {
        Ride ride = rideRepository.getRideById(rideId);
        List<Position> positions = positionRepository.getPositionsByRideId(rideId);
        ride.finishRide(positions);
        return new RideOutput(
                ride.getRideId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getFromLatitude(),
                ride.getFromLongitude(),
                ride.getToLatitude(),
                ride.getToLongitude(),
                ride.getStatus(),
                positions,
                ride.getDistance()
        );
    }

}
