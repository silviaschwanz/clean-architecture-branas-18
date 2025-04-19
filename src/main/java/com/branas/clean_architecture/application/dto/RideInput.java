package com.branas.clean_architecture.application.dto;

public record RideInput(
        String passengerId,
        Double fromLat,
        Double fromLongit,
        Double toLat,
        Double toLongit
) {

}
