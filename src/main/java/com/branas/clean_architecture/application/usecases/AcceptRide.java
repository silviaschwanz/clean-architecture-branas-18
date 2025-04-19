package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.dto.AcceptRideInput;
import com.branas.clean_architecture.application.dto.AcceptRideOutput;
import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.entity.Account;
import com.branas.clean_architecture.domain.entity.Ride;
import org.springframework.stereotype.Service;

@Service
public class AcceptRide {

    private final AccountRepository accountRepository;

    private final RideRepository rideRepository;

    public AcceptRide(AccountRepository accountRepository, RideRepository rideRepository) {
        this.accountRepository = accountRepository;
        this.rideRepository = rideRepository;
    }

    public AcceptRideOutput execute(AcceptRideInput acceptRideInput) {
        Account account = accountRepository.getAccountById(acceptRideInput.driverId());
        if(!account.isDriver()) throw new RuntimeException("Must be a driver");
        Ride ride = rideRepository.getRideById(acceptRideInput.rideId());
        ride.accept(account.getAccountId());
        rideRepository.update(ride);
        return new AcceptRideOutput(ride.getRideId());
    }

}
