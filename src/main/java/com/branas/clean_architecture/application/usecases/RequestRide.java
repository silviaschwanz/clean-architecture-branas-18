package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.account.Account;
import com.branas.clean_architecture.domain.ride.Ride;
import com.branas.clean_architecture.infra.controller.RideInput;
import com.branas.clean_architecture.infra.controller.RideOutput;
import org.springframework.stereotype.Service;

@Service
public class RequestRide {

    private final AccountRepository accountRepository;

    private final RideRepository repository;

    public RequestRide(AccountRepository accountRepository, RideRepository rideRepository) {
        this.accountRepository = accountRepository;
        this.repository = rideRepository;
    }

    public RideOutput execute(RideInput input) {
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
                        input.toLongit()
                )
        );
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
