package com.branas.clean_architecture.infra.repository;

import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.entity.Ride;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideRepositoryMemory implements RideRepository {

    private List<Ride> rides;

    public RideRepositoryMemory() {
        this.rides = new ArrayList<>();
    }

    @Override
    public Ride getRideById(String rideId) {
        return rides.stream().filter(ride -> ride.getRideId().equals(rideId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Ride não encontrada com o rideId informado")
                );
    }

    @Override
    public List<Ride> getRidesByPassanger(String passengerId) {
        var ridesPassenger =  rides.stream()
                .filter(ride -> ride.getPassengerId().equals(passengerId))
                .collect(Collectors.toList());
        if(ridesPassenger.isEmpty()){
            throw new EntityNotFoundException("Nenhuma ride encontrada para o passageiro com ID: " + passengerId);
        }
        return ridesPassenger;
    }

    @Override
    public Ride saveRide(Ride ride) {
        rides.add(ride);
        return ride;
    }

    @Override
    public void update(Ride updateRide) {
        Ride existingRide = rides.stream()
                .filter(ride -> ride.getRideId().equals(updateRide.getRideId()))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("Ride not found")
                );
        rides.set(rides.indexOf(existingRide), updateRide);
    }

}
