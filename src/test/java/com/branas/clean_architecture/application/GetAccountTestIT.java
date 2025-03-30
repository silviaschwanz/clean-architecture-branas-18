package com.branas.clean_architecture.application;

import com.branas.clean_architecture.ContainersConfig;
import com.branas.clean_architecture.driven.adapters.AccountRepositoryPostgres;
import com.branas.clean_architecture.driver.SignupInput;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class GetAccountTestIT {

    @Autowired
    AccountRepositoryPostgres accountDAO;

    @Autowired
    private Flyway flyway;

    @Autowired
    GetAccount getAccount;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @DisplayName("Deve retornar a conta de um passageiro")
    void getAccount(){
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "2568-236",
                true,
                false,
                "123"
        );
        var accountSaved = accountDAO.saveAccount(signupRequestInput);
        var account = getAccount.execute(accountSaved.getAccountId());
        assertEquals(accountSaved.getAccountId(), account.accountId());
        assertEquals("Joao Paulo", account.name());
        assertEquals("joao@gmail.com.br", account.email());
    }

}