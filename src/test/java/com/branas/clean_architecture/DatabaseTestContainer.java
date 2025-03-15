package com.branas.clean_architecture;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ActiveProfiles("test")
public class DatabaseTestContainer {

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgresSqlContainer = new PostgreSQLContainer<>(DockerImageName.parse(
            "postgres:15"
    )).withReuse(true);

    static {
        postgresSqlContainer.start();
    }

}
