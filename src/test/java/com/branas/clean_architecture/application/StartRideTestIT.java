package com.branas.clean_architecture.application;

import com.branas.clean_architecture.ContainersConfig;
import com.branas.clean_architecture.application.dto.AcceptRideInput;
import com.branas.clean_architecture.application.dto.InputStartRide;
import com.branas.clean_architecture.application.dto.OutputStartRide;
import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.application.usecases.AcceptRide;
import com.branas.clean_architecture.application.usecases.StartRide;
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
public class StartRideTestIT {

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

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-01T12:00:00Z");
    private Clock fixedClock;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
        fixedClock = Clock.fixed(FIXED_INSTANT, ZoneId.systemDefault());
    }

    @Test
    void shoudStartRide() {
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        acceptRide.execute(new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId()));
        OutputStartRide outputStartRide = startRide.execute(new InputStartRide(newRide.getRideId()));
        Ride rideStarted = rideRepository.getRideById(outputStartRide.rideId());
        assertEquals(Status.IN_PROGRESS.toString(), rideStarted.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenStatusRideIsNotAccepted(){
        var accountPassanger = accountRepository.saveAccount(
                Account.create(
                        "Joao Paulo",
                        "joao@gmail.com.br",
                        "97456321558",
                        "ABC1234",
                        false,
                        "12345678"
                )
        );
        var accountDriver = accountRepository.saveAccount(
                Account.create(
                        "Luiz Vinicius",
                        "luv@gmail.com.br",
                        "12345678909",
                        "ABC1234",
                        true,
                        "12345691"
                )
        );
        Ride ride = Ride.create(
                accountPassanger.getAccountId(),
                -27.584905257808835,
                -48.545022195325124,
                -27.496887588317275,
                -48.522234807851476,
                fixedClock
        );
        rideRepository.saveRide(ride);
        AcceptRideInput acceptRideInput = new AcceptRideInput(
                ride.getRideId(),
                accountDriver.getAccountId()
        );
        acceptRide.execute(acceptRideInput);
        InputStartRide inputStartRide = new InputStartRide(
                ride.getRideId()
        );
        startRide.execute(inputStartRide);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            startRide.execute(inputStartRide);
        });
        assertNotNull(exception);
        assertEquals("Invalid status", exception.getMessage());
    }


}
