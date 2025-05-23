package com.branas.clean_architecture.domain.service;

import com.branas.clean_architecture.domain.entity.Position;
import com.branas.clean_architecture.domain.vo.Coordinates;

import java.util.List;
import java.util.stream.IntStream;

public class DistanceCalculator {

    public static double calculateDistance(List<Position> positions) {
        return IntStream.range(0, positions.size() - 1)
                .mapToDouble(i -> calculateCordinates(
                                new Coordinates(positions.get(i).getLatitude(), positions.get(i).getLongitude()),
                                new Coordinates(positions.get(i + 1).getLatitude(), positions.get(i + 1).getLongitude())
                        )
                )
                .sum();
    }

    private static int calculateCordinates(Coordinates from, Coordinates to) {
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
