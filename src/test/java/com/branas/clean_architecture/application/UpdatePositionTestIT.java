package com.branas.clean_architecture.application;

import com.branas.clean_architecture.ContainersConfig;
import com.branas.clean_architecture.application.dto.*;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.application.usecases.AcceptRide;
import com.branas.clean_architecture.application.usecases.GetRide;
import com.branas.clean_architecture.application.usecases.StartRide;
import com.branas.clean_architecture.application.usecases.UpdatePosition;
import com.branas.clean_architecture.data.AccountFactory;
import com.branas.clean_architecture.data.RideFactory;
import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.entity.Account;
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
public class UpdatePositionTestIT {

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
    GetRide getRide;

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-01T12:00:00Z");
    private Clock fixedClock;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
        fixedClock = Clock.fixed(FIXED_INSTANT, ZoneId.systemDefault());
    }

    @Test
    void shoudUpdatePosition() {
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

        OutputRide rideOutput = getRide.execute(newRide.getRideId());
        assertEquals(30, rideOutput.distance());
        assertEquals(Status.IN_PROGRESS.toString(), rideOutput.status());
    }

    @Test
    void shouldThrowExceptionWhenStatusRideIsNotAccepted(){
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        });
        assertNotNull(exception);
        assertEquals("Invalid status", exception.getMessage());
    }

}
