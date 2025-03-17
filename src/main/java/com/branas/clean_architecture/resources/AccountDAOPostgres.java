package com.branas.clean_architecture.resources;

import com.branas.clean_architecture.driver.AccountResponse;
import com.branas.clean_architecture.driver.SignupRequestInput;
import com.branas.clean_architecture.driver.SignupResponse;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Service
public class AccountDAOPostgres implements AccountDAO{

    private final DataSource dataSource;

    public AccountDAOPostgres(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public AccountResponse getAccountById(UUID accountId) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from account where account_id = ?");
            ps.setObject(1, accountId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) return new AccountResponse(
                        rs.getObject("account_id", UUID.class),
                        rs.getString("email"),
                        rs.getString("name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar conta pelo id: " + accountId, e);
        }
        return null;
    }

    public AccountResponse getAccountByEmail(String email) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from account where email = ?");
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) return new AccountResponse(
                        rs.getObject("account_id", UUID.class),
                        rs.getString("email"),
                        rs.getString("name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar conta pelo e-mail: " + email, e);
        }
        return null;
    }

    public SignupResponse saveAccount(SignupRequestInput signupRequestInput) {
        UUID id = UUID.randomUUID();
        String passwordAlgorithm = UUID.randomUUID().toString();
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
            insertStatement.setString(9, passwordAlgorithm);
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted == 0) {
                throw new RuntimeException("Falha ao inserir conta, nenhuma linha foi afetada.");
            }
            return new SignupResponse(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar conta", e);
        }
    }

}
