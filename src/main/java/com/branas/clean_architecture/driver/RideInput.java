package com.branas.clean_architecture.driver;

public record RideInput(
        String passengerId,
        Double fromLat,
        Double fromLongit,
        Double toLat,
        Double toLongit
) {

}
