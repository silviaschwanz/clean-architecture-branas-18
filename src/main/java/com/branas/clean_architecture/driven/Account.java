package com.branas.clean_architecture.driven;

import java.util.UUID;

public record Account(
        UUID accountId,
        String name,
        String email,
        String cpf,
        String carPlate,
        Boolean isPassenger,
        Boolean isDriver,
        String password,
        String passwordAlgorithm
) {

}
