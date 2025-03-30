package com.branas.clean_architecture.driven;

import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.driver.RideInput;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RideDAOMemory implements RideRepository {

    private List<Ride> rides;

    public RideDAOMemory() {
        this.rides = new ArrayList<>();
    }

    @Override
    public Ride getRideById(UUID rideId) {
        return rides.stream().filter(ride -> ride.rideId().equals(rideId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Ride não encontrada com o rideId informado")
                );
    }

    @Override
    public List<Ride> getRidesByPassanger(UUID passengerId) {
        var ridesPassenger =  rides.stream()
                .filter(ride -> ride.passengerId().equals(passengerId))
                .collect(Collectors.toList());
        if(ridesPassenger.isEmpty()){
            throw new EntityNotFoundException("Nenhuma ride encontrada para o passageiro com ID: " + passengerId);
        }
        return ridesPassenger;
    }

    @Override
    public UUID saveRide(RideInput rideInput) {
   /*     Ride ride = new Ride(
                UUID.randomUUID().toString(),
                rideInput.passengerId(),
                null,
                "SOLICITADA",
                null,
                rideInput.from().latitude(),
                rideInput.from().longitude(),
                rideInput.to().latitude(),
                rideInput.to().longitude(),
                null,
                LocalDateTime.now()
        );
        rides.add(ride);
        return ride.rideId();*/
        return null;
    }

}
