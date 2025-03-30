package com.branas.clean_architecture.driver;

public record RideInput(
        String passengerId,
        Position from,
        Position to
) {

}
