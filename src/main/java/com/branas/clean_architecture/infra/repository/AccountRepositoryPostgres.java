package com.branas.clean_architecture.infra.repository;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.domain.account.Account;
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
public class AccountRepositoryPostgres implements AccountRepository {

    private final DataSource dataSource;

    public AccountRepositoryPostgres(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void emailNotRegistered(String email) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from account where email = ?");
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    throw new IllegalStateException("There is already an account with that email");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for account by email: " + email, e);
        }
    }

    public Account getAccountById(String accountId) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from account where account_id = ?");
            ps.setObject(1, UUID.fromString(accountId));
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) return Account.restore(
                        rs.getObject("account_id", UUID.class).toString(),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("cpf"),
                        rs.getString("car_plate"),
                        rs.getBoolean("is_driver")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for account by id: " + accountId, e);
        }
        throw new EntityNotFoundException("Account não encontrada com o accountId informado");
    }

    public Account getAccountByEmail(String email) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from account where email = ?");
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) return Account.restore(
                        rs.getObject("account_id", UUID.class).toString(),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("cpf"),
                        rs.getString("car_plate"),
                        rs.getBoolean("is_driver")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for account by email: " + email, e);
        }
        throw new EntityNotFoundException("Account not found");
    }

    public Account saveAccount(Account account) {
        try (Connection con = dataSource.getConnection()) {
            final PreparedStatement insertStatement = con.prepareStatement(
                    "insert into account " +
                            "(account_id, name, email, cpf, car_plate, is_passenger, is_driver, password_algorithm)" +
                            " values (?, ?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setObject(1, UUID.fromString(account.getAccountId()));
            insertStatement.setString(2, account.getName());
            insertStatement.setString(3, account.getEmail());
            insertStatement.setString(4, account.getCpf());
            insertStatement.setString(5, account.getCarPlate());
            insertStatement.setBoolean(6, account.isPassenger());
            insertStatement.setBoolean(7, account.isDriver());
            insertStatement.setString(8, account.getPassword());
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted == 0) {
                throw new RuntimeException("Failed to insert account, no rows affected");
            }
            return getAccountByEmail(account.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account", e);
        }
    }
}
