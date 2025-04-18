package com.branas.clean_architecture.infra.controller;

public record RideInput(
        String passengerId,
        Double fromLat,
        Double fromLongit,
        Double toLat,
        Double toLongit
) {

}
