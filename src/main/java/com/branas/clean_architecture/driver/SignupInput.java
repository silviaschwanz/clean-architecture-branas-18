package com.branas.clean_architecture.driver;

public record SignupInput(
        String name,
        String email,
        String cpf,
        String carPlate,
        boolean isPassenger,
        boolean isDriver,
        String password
) {
}
