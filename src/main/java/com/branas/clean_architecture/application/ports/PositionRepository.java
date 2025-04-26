package com.branas.clean_architecture.application.ports;

import com.branas.clean_architecture.domain.entity.Position;

import java.util.List;

public interface PositionRepository {

    Position savePosition(Position position);
    List<Position> getPositionsByRideId(String rideId);

}
