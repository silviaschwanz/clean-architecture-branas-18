package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.dto.RequestRideOutput;
import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.entity.Account;
import com.branas.clean_architecture.domain.entity.Ride;
import com.branas.clean_architecture.application.dto.RideInput;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class RequestRide {

    private final AccountRepository accountRepository;

    private final RideRepository repository;

    private final Clock clock;

    public RequestRide(AccountRepository accountRepository, RideRepository rideRepository, Clock clock) {
        this.accountRepository = accountRepository;
        this.repository = rideRepository;
        this.clock = clock;
    }

    public RequestRideOutput execute(RideInput input) {
        Account account = accountRepository.getAccountById(input.passengerId());
        if(!account.isPassenger()) {
            throw new RuntimeException("Must be a passenger");
        }
        Ride ride = repository.saveRide(
                Ride.create(
                        account.getAccountId(),
                        input.fromLat(),
                        input.fromLongit(),
                        input.toLat(),
                        input.toLongit(),
                        clock
                )
        );
        return new RequestRideOutput(
                ride.getRideId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getFromLatitude(),
                ride.getFromLongitude(),
                ride.getToLatitude(),
                ride.getToLongitude(),
                ride.getStatus()
        );
    }

}
