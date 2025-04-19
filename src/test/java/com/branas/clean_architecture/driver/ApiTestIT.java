package com.branas.clean_architecture.driver;

import com.branas.clean_architecture.ContainersConfig;
import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.domain.entity.Account;
import com.branas.clean_architecture.application.dto.SignupInput;
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
    AccountRepository accountRepository;

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
                "ABC1234",
                true,
                false,
                "12345678"
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
                "ABC1234",
                true,
                false,
                "12345678"
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
                "12345678"
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
                "12345678"
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
                "12345678"
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
        accountRepository.saveAccount(
                Account.create(
                        "Joao Paulo",
                        "joao@gmail.com.br",
                        "97456321558",
                        "ABC1234",
                        true,
                        "12345678"
                )
        );
        var signupRequestInput = new SignupInput(
                "joao",
                "joao@gmail.com.br",
                "97456321558",
                "0",
                true,
                false,
                "12345678"
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
        var accountSaved = accountRepository.saveAccount(
                Account.create(
                        "Joao Paulo",
                        "joao@gmail.com.br",
                        "97456321558",
                        "ABC1234",
                        true,
                        "12345678"
                )
        );
        var account = accountRepository.getAccountByEmail(accountSaved.getEmail());

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/signup/" + account.getAccountId())
                .then()
                .statusCode(200)
                .body("name", is("Joao Paulo"))
                .body("accountId", is(account.getAccountId()))
                .body("email", is("joao@gmail.com.br"));
    }

}