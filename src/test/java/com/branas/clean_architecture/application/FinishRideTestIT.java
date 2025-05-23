package com.branas.clean_architecture.application;

import com.branas.clean_architecture.ContainersConfig;
import com.branas.clean_architecture.application.dto.*;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.application.usecases.*;
import com.branas.clean_architecture.data.AccountFactory;
import com.branas.clean_architecture.data.RideFactory;
import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.entity.Ride;
import com.branas.clean_architecture.infra.repository.postgres.AccountRepositoryPostgres;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
public class FinishRideTestIT {

    @Autowired
    UpdatePosition updatePosition;

    @Autowired
    AccountRepositoryPostgres accountRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    private Flyway flyway;

    @Autowired
    AcceptRide acceptRide;

    @Autowired
    StartRide startRide;

    @Autowired
    FinishRide finishRide;

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-01T12:00:00Z");
    private Clock fixedClock;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
        fixedClock = Clock.fixed(FIXED_INSTANT, ZoneId.systemDefault());
    }

    @Test
    void shouldFinishRide() {
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        acceptRide.execute(new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId()));
        startRide.execute(new InputStartRide(newRide.getRideId()));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));

        InputFinishRide inputFinishRide = new InputFinishRide(newRide.getRideId());
        OutputRide outputRide = finishRide.execute(inputFinishRide);
        assertEquals(30, outputRide.distance());
        assertEquals(Status.COMPLETED.toString(), outputRide.status());
        assertEquals(60, outputRide.fare());
    }

    @Test
    void shouldThrowExceptionWhenStatusRideIsNotInProgress(){
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        acceptRide.execute(new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId()));
        InputFinishRide inputFinishRide = new InputFinishRide(newRide.getRideId());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            finishRide.execute(inputFinishRide);
        });
        assertNotNull(exception);
        assertEquals("Invalid status", exception.getMessage());
    }

}
