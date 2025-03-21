package com.branas.clean_architecture;

import com.branas.clean_architecture.application.Signup;
import com.branas.clean_architecture.driven.Account;
import com.branas.clean_architecture.driver.SignupRequestInput;
import com.branas.clean_architecture.driver.SignupResponse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class AccountTestIT {

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
    @DisplayName("Deve criar a conta de um passageiro")
    void executeSignup(){
        var signupRequestInput = new SignupRequestInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "2568-236",
                true,
                false,
                "123"
        );
        Account account = signup.execute(signupRequestInput);
        assertNotNull(account.accountId());
    }

    @Test
    @DisplayName("Não deve criar a conta de um passageiro com nome inválido")
    void signupInvalidName(){
        var signupRequestInput = new SignupRequestInput(
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
        assertEquals("Nome inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar a conta de um passageiro com email inválido")
    void signupInvalidEmail(){
        var signupRequestInput = new SignupRequestInput(
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
        assertEquals("Email inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar a conta de um passageiro com cpf inválido - com 10 digítos")
    void signupInvalidCpf(){
        var signupRequestInput = new SignupRequestInput(
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
        assertEquals("Cpf inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar a conta de um motorista com placa inválido")
    void signupInvalidCarPlate(){
        var signupRequestInput = new SignupRequestInput(
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
        assertEquals("Placa do carro inválida", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar conta de um passageiro com email duplicado")
    void signupDuplicateAccount() {
        var signupRequestInput = new SignupRequestInput(
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
        assertEquals("Já existe uma conta com o email informado", exception.getMessage());
    }

}