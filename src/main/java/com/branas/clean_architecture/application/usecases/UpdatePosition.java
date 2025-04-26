package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.dto.InputUpdatePosition;
import com.branas.clean_architecture.application.ports.PositionRepository;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.entity.Position;
import com.branas.clean_architecture.domain.entity.Ride;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class UpdatePosition {

    private final RideRepository rideRepository;
    private final PositionRepository positionRepository;
    private final Clock clock;

    public UpdatePosition(RideRepository rideRepository, PositionRepository positionRepository, Clock clock) {
        this.rideRepository = rideRepository;
        this.positionRepository = positionRepository;
        this.clock = clock;
    }

    public void execute(InputUpdatePosition inputUpdatePosition) {
        Ride ride = rideRepository.getRideById(inputUpdatePosition.rideId());
        Position position = Position.create(
                ride.getRideId(),
                inputUpdatePosition.latitude(),
                inputUpdatePosition.longitude(),
                clock
        );
        positionRepository.savePosition(position);
    }

}
