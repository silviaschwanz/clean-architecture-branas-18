package com.branas.clean_architecture.infra.controller;

import com.branas.clean_architecture.SignupDatabase;
import com.branas.clean_architecture.SignupRequestInput;
import com.branas.clean_architecture.ValidateCpf;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/signup")
public class Signup {

    @Autowired
    DataSource dataSource;

    @PostMapping()
    public ResponseEntity<?> signup(@RequestBody SignupRequestInput signupRequestInput) {
        var result = "";
        ObjectMapper mapper = new ObjectMapper();
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
                result = mapper.writeValueAsString(Map.of("error", "O email já existe"));
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(result);
            }
            String id = UUID.randomUUID().toString();
            String passwordAlgorithm = UUID.randomUUID().toString();
            if(!signupRequestInput.name().matches("[a-zA-Z]+\\s[a-zA-Z]+")) {
                result = mapper.writeValueAsString(Map.of("error", "Nome inválido"));
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(result);
            }
            if(!signupRequestInput.email().matches("^(.+)@(.+)$")) {
                result = mapper.writeValueAsString(Map.of("error", "Email inválido"));
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(result);
            }
            if(!new ValidateCpf().validate(signupRequestInput.cpf())) {
                result = mapper.writeValueAsString(Map.of("error", "Cpf inválido"));
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(result);
            }
            if(signupRequestInput.isDriver() && !signupRequestInput.carPlate().matches("[A-Z]{3}[0-9]{4}")){
                result = mapper.writeValueAsString(Map.of("error", "Placa do carro inválida"));
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(result);

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
            result = mapper.writeValueAsString(Map.of("accountId", id));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        } catch (Exception e){
            e.printStackTrace(); // ou use um logger
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable UUID accountId) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from account where account_id = ?");
        ) {
            ps.setObject(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> accountData = new HashMap<>();
                    accountData.put("id", rs.getObject("account_id"));
                    accountData.put("email", rs.getString("email"));
                    accountData.put("name", rs.getString("name"));
                    return ResponseEntity.ok(accountData);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
                }
            }

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error: " + e.getMessage());
        }
    }

}


