package com.branas.clean_architecture.application;

import com.branas.clean_architecture.ContainersConfig;
import com.branas.clean_architecture.application.usecases.Signup;
import com.branas.clean_architecture.domain.account.Account;
import com.branas.clean_architecture.driver.SignupInput;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class SignupTestIT {

    @Autowired
    Signup signup;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldSignupValidPassenger(){
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "2568-236",
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