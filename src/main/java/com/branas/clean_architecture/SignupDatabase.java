package com.branas.clean_architecture;

public record SignupDatabase(
        String account_id,
        String name,
        String email,
        String cpf,
        String car_plate,
        boolean is_passenger,
        boolean is_driver
) {
}
