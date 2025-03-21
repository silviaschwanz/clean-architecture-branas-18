package com.branas.clean_architecture.application;

import com.branas.clean_architecture.driven.AccountDAOMemory;
import com.branas.clean_architecture.driven.PasswordService;
import com.branas.clean_architecture.driver.SignupRequestInput;
import com.branas.clean_architecture.driver.SignupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    AccountDAOMemory accountDAO;
    Signup signup;

    @BeforeEach
    public void setUp() {
        accountDAO = new AccountDAOMemory();
        signup = new Signup(accountDAO);
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
        SignupResponse result = signup.execute(signupRequestInput);
        assertNotNull(result.accountId);
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