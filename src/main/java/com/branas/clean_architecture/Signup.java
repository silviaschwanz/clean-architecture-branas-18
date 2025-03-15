package com.branas.clean_architecture;

import com.branas.clean_architecture.driver.AccountResponse;
import com.branas.clean_architecture.driver.SignupRequestInput;
import com.branas.clean_architecture.driver.SignupResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class Signup {

    @Autowired
    DataSource dataSource;

    public SignupResponse execute(SignupRequestInput signupRequestInput) {
        List<SignupDatabase> signupsByEmailDatabase = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("select * from account where email = ?");) {
            ps.setString(1, signupRequestInput.email());
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    signupsByEmailDatabase.add(new SignupDatabase(
                            rs.getString("account_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("cpf"),
                            rs.getString("car_plate"),
                            rs.getBoolean("is_passenger"),
                            rs.getBoolean("is_driver")
                    ));
                }
            }
            if(!signupsByEmailDatabase.isEmpty()) {
                throw new IllegalArgumentException("O email já existe");
            }
            String id = UUID.randomUUID().toString();
            String passwordAlgorithm = UUID.randomUUID().toString();
            if(!signupRequestInput.name().matches("[a-zA-Z]+\\s[a-zA-Z]+")) {
                throw new IllegalArgumentException("Nome inválido");
            }
            if(!signupRequestInput.email().matches("^(.+)@(.+)$")) {
                throw new IllegalArgumentException("Email inválido");
            }
            if(!new ValidateCpf().validate(signupRequestInput.cpf())) {
                throw new IllegalArgumentException("Cpf inválido");
            }
            if(signupRequestInput.isDriver() && !signupRequestInput.carPlate().matches("[A-Z]{3}[0-9]{4}")){
                throw new IllegalArgumentException("Placa do carro inválida");
            }
            final PreparedStatement insertStatement = con.prepareStatement("insert into account (account_id, name, email, cpf, car_plate, is_passenger, is_driver, password, password_algorithm) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setObject(1, id, java.sql.Types.OTHER);
            insertStatement.setString(2, signupRequestInput.name());
            insertStatement.setString(3, signupRequestInput.email());
            insertStatement.setString(4, signupRequestInput.cpf());
            insertStatement.setString(5, signupRequestInput.carPlate());
            insertStatement.setBoolean(6, signupRequestInput.isPassenger());
            insertStatement.setBoolean(7, signupRequestInput.isDriver());
            insertStatement.setString(8, signupRequestInput.password());
            insertStatement.setString(9, passwordAlgorithm);
            insertStatement.executeUpdate();
            return new SignupResponse(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public AccountResponse getAccount(@PathVariable UUID accountId) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from account where account_id = ?");
        ) {
            ps.setObject(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("Não foi encontrada conta com o accountId informado");
                }
                return new AccountResponse(
                        rs.getObject("account_id").toString(),
                        rs.getString("email"),
                        rs.getString("name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}


