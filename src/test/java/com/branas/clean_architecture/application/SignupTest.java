package com.branas.clean_architecture.application;

import com.branas.clean_architecture.application.ports.MailerGateway;
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
    void shouldNotSignupInvalidName(){
        var signupRequestInput = new SignupInput(
                "joao 123",
                "joao@gmail.com.br",
                "97456321558",
                "2568-236",
                true,
                false,
                "123"
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            signup.execute(signupRequestInput);
        });
        assertEquals("Invalid name. Only letters and spaces are allowed", exception.getMessage());
    }

    @Test
    void shouldNotSignupInvalidEmail(){
        var signupRequestInput = new SignupInput(
                "joao Paulo",
                "gmail",
                "97456321558",
                "2568-236",
                true,
                false,
                "123"
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            signup.execute(signupRequestInput);
        });
        assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    void shouldNotSignupInvalidCpf(){
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "9745632155",
                "",
                true,
                false,
                "123"
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            signup.execute(signupRequestInput);
        });
        assertEquals("CPF is invalid lenght", exception.getMessage());
    }

    @Test
    void shouldNotSignupInvalidCarPlate(){
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "A23",
                false,
                true,
                "123"
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            signup.execute(signupRequestInput);
        });
        assertEquals("Invalid car plate", exception.getMessage());
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