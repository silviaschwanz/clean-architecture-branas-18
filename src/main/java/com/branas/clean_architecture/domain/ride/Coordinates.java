package com.branas.clean_architecture.domain.ride;

public record Coordinates(double latitude, double longitude) {

    public Coordinates {
        if (latitude < -90 || latitude > 90) throw new Error("Invalid latitude");
        if (longitude < -180 || longitude > 180) throw new Error("Invalid longitude");
    }

}
