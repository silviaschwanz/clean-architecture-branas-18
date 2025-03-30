package com.branas.clean_architecture.driver;

import com.branas.clean_architecture.ContainersConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class ApiTestIT {

    @LocalServerPort
    int port;

    @Autowired
    DataSource dataSource;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldSignupAccountPassanger(){
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
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
                .body("accountId", notNullValue())
                .extract().response();
        response.prettyPrint();
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
        given()
                .contentType(ContentType.JSON)
                .body(signupRequestInput)
                .when()
                .post("/signup")
                .then()
                .statusCode(422)
                .body("error", is("Invalid name. Only letters and spaces are allowed"));
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
        given()
                .contentType(ContentType.JSON)
                .body(signupRequestInput)
                .when()
                .post("/signup")
                .then()
                .statusCode(422)
                .body("error", is("Invalid email"));
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
        given()
                .contentType(ContentType.JSON)
                .body(signupRequestInput)
                .when()
                .post("/signup")
                .then()
                .statusCode(422)
                .body("error", is("CPF is invalid lenght"));
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
        given()
                .contentType(ContentType.JSON)
                .body(signupRequestInput)
                .when()
                .post("/signup")
                .then()
                .statusCode(422)
                .body("error", is("Invalid car plate"));
    }

    @Test
    void shouldNotSignupDuplicateAccount() {
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
        var signupRequestInput = new SignupInput(
                "joao",
                "joao@gmail.com.br",
                "97456321558",
                "0",
                true,
                false,
                "123"
        );
        given()
                .contentType(ContentType.JSON)
                .body(signupRequestInput)
                .when()
                .post("/signup")
                .then()
                .statusCode(422)
                .body("error", is("There is already an account with that email"));
    }

    @Test
    void shouldGetAccount(){
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
                .body("accountId", is(accountUuid.toString()))
                .body("email", is("joao@gmail.com.br"));
    }

}