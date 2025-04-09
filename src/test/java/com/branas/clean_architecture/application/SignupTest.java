package com.branas.clean_architecture.application;

import com.branas.clean_architecture.application.ports.MailerGateway;
import com.branas.clean_architecture.application.usecases.Signup;
import com.branas.clean_architecture.domain.Account;
import com.branas.clean_architecture.driven.adapters.AccountRepositoryMemory;
import com.branas.clean_architecture.driven.adapters.MailerGatewayMemory;
import com.branas.clean_architecture.driver.SignupInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupTest {

    AccountRepositoryMemory accountDAO;

    MailerGateway mailerGateway;

    Signup signup;

    @BeforeEach
    public void setUp() {
        accountDAO = new AccountRepositoryMemory();
        mailerGateway = new MailerGatewayMemory();
        signup = new Signup(accountDAO, mailerGateway);
    }

    @Test
    @DisplayName("Deve criar a conta de um passageiro")
    void shouldSignupAccountPassenger(){
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "ABC1234",
                true,
                false,
                "123"
        );
        Account account = signup.execute(signupRequestInput);
        assertNotNull(account.getAccountId());
    }

    @Test
    void shouldNotSignupDuplicateAccount() {
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "0",
                true,
                false,
                "123"
        );
        signup.execute(signupRequestInput);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            signup.execute(signupRequestInput);
        });
        assertEquals("There is already an account with that email", exception.getMessage());
    }

}