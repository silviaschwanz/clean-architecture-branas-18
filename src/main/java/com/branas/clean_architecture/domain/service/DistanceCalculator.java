package com.branas.clean_architecture.domain.service;

import com.branas.clean_architecture.domain.vo.Coordinates;

public class DistanceCalculator {

    public static int calculate(Coordinates from, Coordinates to) {
        final double earthRadius = 6371.0;
        final double degreesToRadians = Math.PI / 180;
        double deltaLat = (to.latitude() - from.latitude()) * degreesToRadians;
        double deltaLon = (to.longitude() - from.longitude()) * degreesToRadians;
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(from.latitude() * degreesToRadians)
                * Math.cos(to.latitude() * degreesToRadians)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;
        return (int) Math.round(distance);
    }

}
