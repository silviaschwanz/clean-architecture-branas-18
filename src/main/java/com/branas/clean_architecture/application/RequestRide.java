package com.branas.clean_architecture.application;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.Account;
import com.branas.clean_architecture.driver.RideInput;
import com.branas.clean_architecture.driver.RideOutput;
import org.springframework.stereotype.Service;

@Service
public class RequestRide {

    private AccountRepository accountDAO;

    private RideRepository rideDAO;

    public RequestRide(AccountRepository accountDAO, RideRepository rideDAO) {
        this.accountDAO = accountDAO;
        this.rideDAO = rideDAO;
    }

    public RideOutput execute(RideInput input) {
        Account account = accountDAO.getAccountById(input.passengerId());
        if(!account.isPassenger()) {
            throw new RuntimeException("A conta precisa ser de um passageiro");
        }
        //Ride ride =
        return null;
    }

}
