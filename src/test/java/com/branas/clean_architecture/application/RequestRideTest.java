package com.branas.clean_architecture.application;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.application.usecases.RequestRide;
import com.branas.clean_architecture.domain.account.Account;
import com.branas.clean_architecture.domain.ride.Ride;
import com.branas.clean_architecture.infra.controller.RideInput;
import com.branas.clean_architecture.infra.controller.RideOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestRideTest {

    private AccountRepository accountRepository;
    private RideRepository rideRepository;
    private RequestRide requestRide;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        rideRepository = mock(RideRepository.class);
        requestRide = new RequestRide(accountRepository, rideRepository);
    }

    @Test
    void shouldCreateRideWhenAccountIsPassenger() {
        String passengerId = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";
        RideInput input = new RideInput(passengerId, 10.0, -10.0, 20.0, -20.0);
        Account account = mock(Account.class);
        when(account.getAccountId()).thenReturn(passengerId);
        when(account.isPassenger()).thenReturn(true);
        Ride ride = Ride.create(passengerId, 10.0, -10.0, 20.0, -20.0);
        when(accountRepository.getAccountById(passengerId)).thenReturn(account);
        when(rideRepository.saveRide(any(Ride.class))).thenReturn(ride);
        RideOutput output = requestRide.execute(input);
        assertNotNull(output);
        assertEquals(passengerId, output.passengerId());
        assertEquals(10.0, output.fromLatitude());
        assertEquals(-10.0, output.fromLongitude());
        assertEquals(20.0, output.toLatitude());
        assertEquals(-20.0, output.toLongitude());
        assertEquals("REQUESTED", output.status());
        verify(accountRepository).getAccountById(passengerId);
        verify(rideRepository).saveRide(any(Ride.class));
    }

    @Test
    void shouldThrowExceptionWhenAccountIsNotPassenger() {
        String passengerId = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";
        RideInput input = new RideInput(passengerId, 10.0, -10.0, 20.0, -20.0);
        Account account = mock(Account.class);
        when(account.isPassenger()).thenReturn(false);
        when(accountRepository.getAccountById(passengerId)).thenReturn(account);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            requestRide.execute(input);
        });
        assertEquals("Must be a passenger", exception.getMessage());
        verify(accountRepository).getAccountById(passengerId);
        verify(rideRepository, never()).saveRide(any());
    }

}