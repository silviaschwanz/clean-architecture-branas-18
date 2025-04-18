package com.branas.clean_architecture.infra.controller;

public record AccountOutput(
        String accountId,
        String name,
        String email,
        String cpf,
        String carPlate,
        Boolean isPassenger,
        Boolean isDriver
) {
}
