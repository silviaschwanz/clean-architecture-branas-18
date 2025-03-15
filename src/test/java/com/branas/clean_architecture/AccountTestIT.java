package com.branas.clean_architecture;

import com.branas.clean_architecture.application.Signup;
import com.branas.clean_architecture.driver.AccountResponse;
import com.branas.clean_architecture.driver.SignupRequestInput;
import com.branas.clean_architecture.driver.SignupResponse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountTestIT extends DatabaseTestContainer {

    @Autowired
    Signup signup;

    @Autowired
    DataSource dataSource;

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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            signup.execute(signupRequestInput);
        });
        assertEquals("O email já existe", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar a conta de um passageiro")
    void getAccount(){
        var accountUuid = UUID.randomUUID();
        try (Connection con = dataSource.getConnection()){
            PreparedStatement insertStatement = con.prepareStatement("insert into account (account_id, name, email, cpf, car_plate, is_passenger, is_driver, password, password_algorithm) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setObject(1, accountUuid, java.sql.Types.OTHER);
            insertStatement.setString(2, "joao");
            insertStatement.setString(3, "joao@gmail.com.br");
            insertStatement.setString(4, "97456321558");
            insertStatement.setString(5, "0");
            insertStatement.setBoolean(6, true);
            insertStatement.setBoolean(7, false);
            insertStatement.setString(8, "1236");
            insertStatement.setString(9, "dfdklfjkdsljfklsdajfkldjsf");
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        AccountResponse account = signup.getAccount(accountUuid);
        assertEquals(accountUuid.toString(), account.accountId());
        assertEquals("joao", account.name());
        assertEquals("joao@gmail.com.br", account.email());
    }

}