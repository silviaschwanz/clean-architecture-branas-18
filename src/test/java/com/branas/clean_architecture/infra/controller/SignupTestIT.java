package com.branas.clean_architecture.infra.controller;

import com.branas.clean_architecture.DatabaseTestContainer;
import com.branas.clean_architecture.SignupRequestInput;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignupTestIT extends DatabaseTestContainer{

    @LocalServerPort
    int port;

    @Autowired
    DataSource dataSource;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Deve criar a conta de um passageiro")
    void signup(){

        var signupRequestInput = new SignupRequestInput(
                "joao",
                "joao@gmail.com.br",
                "97456321558",
                "2568-236",
                true,
                false,
                "123"
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(signupRequestInput)
                .when()
                .post("/signup")
                .then()
                .statusCode(200)
                /* .body("uuid", notNullValue())
                 .body("valor", is("4000.00"))
                 .body("chavePixOrigem", is("frt@gmail.com"))
                 .body("chavePixDestino", is("gil@gmail.com"))*/
                .extract().response();
        response.prettyPrint();
    }

    @Test
    @DisplayName("Deve criar a conta de um passageiro")
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
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/signup/" + accountUuid)
                .then()
                .statusCode(200)
                .body("name", is("joao"))
                .body("id", is(accountUuid.toString()))
                .body("email", is("joao@gmail.com.br"));
    }

}