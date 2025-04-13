package com.branas.clean_architecture.application;

import com.branas.clean_architecture.ContainersConfig;
import com.branas.clean_architecture.application.usecases.GetAccount;
import com.branas.clean_architecture.application.usecases.Signup;
import com.branas.clean_architecture.domain.account.Account;
import com.branas.clean_architecture.driven.adapters.AccountRepositoryPostgres;
import com.branas.clean_architecture.driver.RideInput;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class RequestRideTest {

    @Autowired
    AccountRepositoryPostgres accountRepository;

    @Autowired
    private Flyway flyway;

    @Autowired
    RequestRide requestRide;

    @Autowired
    Signup signup;

    @Autowired
    GetAccount getAccount;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldRequestRide(){
        var account = accountRepository.saveAccount(
                Account.create(
                        "Joao Paulo",
                        "joao@gmail.com.br",
                        "97456321558",
                        "ABC1234",
                        false,
                        "123"
                )
        );
        var rideInput = new RideInput(
                account.getAccountId(),
                -27.584905257808835,
                -48.545022195325124,
                -27.496887588317275,
                -48.522234807851476
        );
        var rideOutput = requestRide.execute(rideInput);
        assertEquals(account.getAccountId(), rideOutput.passengerId());
    }

}