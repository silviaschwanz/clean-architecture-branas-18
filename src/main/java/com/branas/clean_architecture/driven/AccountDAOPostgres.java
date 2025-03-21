package com.branas.clean_architecture.driven;

import com.branas.clean_architecture.application.AccountDAO;
import com.branas.clean_architecture.driver.SignupRequestInput;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Service
@Primary
public class AccountDAOPostgres implements AccountDAO {

    private final DataSource dataSource;

    public AccountDAOPostgres(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void accountAlreadyExists(String email) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from account where email = ?");
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    throw new IllegalStateException("Já existe uma conta com o email informado");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar conta pelo e-mail: " + email, e);
        }
    }

    public Account getAccountById(UUID accountId) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from account where account_id = ?");
            ps.setObject(1, accountId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) return new Account(
                        rs.getObject("account_id", UUID.class),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("cpf"),
                        rs.getString("car_plate"),
                        rs.getBoolean("is_passenger"),
                        rs.getBoolean("is_driver"),
                        rs.getString("password"),
                        rs.getString("password_algorithm")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar conta pelo id: " + accountId, e);
        }
        throw new EntityNotFoundException("Account não encontrada com o accountId informado");
    }

    public Account getAccountByEmail(String email) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from account where email = ?");
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) return new Account(
                        rs.getObject("account_id", UUID.class),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("cpf"),
                        rs.getString("car_plate"),
                        rs.getBoolean("is_passenger"),
                        rs.getBoolean("is_driver"),
                        rs.getString("password"),
                        rs.getString("password_algorithm")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar conta pelo e-mail: " + email, e);
        }
        throw new EntityNotFoundException("Account não encontrada com o email informado");
    }

    public UUID saveAccount(SignupRequestInput signupRequestInput) {
        UUID id = UUID.randomUUID();
        try (Connection con = dataSource.getConnection()) {
            final PreparedStatement insertStatement = con.prepareStatement(
                    "insert into account " +
                            "(account_id, name, email, cpf, car_plate, is_passenger, is_driver, password, password_algorithm)" +
                            " values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setObject(1, id);
            insertStatement.setString(2, signupRequestInput.name());
            insertStatement.setString(3, signupRequestInput.email());
            insertStatement.setString(4, signupRequestInput.cpf());
            insertStatement.setString(5, signupRequestInput.carPlate());
            insertStatement.setBoolean(6, signupRequestInput.isPassenger());
            insertStatement.setBoolean(7, signupRequestInput.isDriver());
            insertStatement.setString(8, signupRequestInput.password());
            insertStatement.setString(9, PasswordService.encodePassword(signupRequestInput.password()));
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted == 0) {
                throw new RuntimeException("Falha ao inserir conta, nenhuma linha foi afetada.");
            }
            return id;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar conta", e);
        }
    }
}
