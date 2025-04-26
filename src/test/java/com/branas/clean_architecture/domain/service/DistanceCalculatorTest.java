package com.branas.clean_architecture.domain.service;

import com.branas.clean_architecture.domain.vo.Coordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanceCalculatorTest {

    @Test
    void testDistanceBetweenSamePointsShouldBeZero() {
        Coordinates point = new Coordinates(0.0, 0.0);
        int distance = DistanceCalculator.calculate(point, point);
        assertEquals(0, distance);
    }

    @Test
    void testDistanceBetweenKnownPoints() {
        // Exemplo: São Paulo (lat: -23.5505, lon: -46.6333) para Rio de Janeiro (lat: -22.9068, lon: -43.1729)
        Coordinates saoPaulo = new Coordinates(-23.5505, -46.6333);
        Coordinates rioDeJaneiro = new Coordinates(-22.9068, -43.1729);
        int distance = DistanceCalculator.calculate(saoPaulo, rioDeJaneiro);
        assertTrue(distance >= 350 && distance <= 370, "Distância esperada entre 350 e 370 km");
    }

    @Test
    void testDistanceWithNegativeCoordinates() {
        Coordinates point1 = new Coordinates(-10.0, -10.0);
        Coordinates point2 = new Coordinates(10.0, 10.0);
        int distance = DistanceCalculator.calculate(point1, point2);
        assertTrue(distance > 0, "Distância deve ser maior que zero");
    }

}