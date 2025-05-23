package com.branas.clean_architecture.domain.service;

import com.branas.clean_architecture.domain.entity.Position;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistanceCalculatorTest {

    @Test
    void testDistanceBetweenSamePointsShouldBeZero() {
        List<Position> positions = new ArrayList<>();
        Position position = Position.restore(
                UUID.randomUUID(), UUID.randomUUID(), 0.0, 0.0, LocalDateTime.now()
        );
        positions.add(position);
        double distance = DistanceCalculator.calculateDistance(positions);
        assertEquals(0, distance);
    }

    @Test
    void testDistanceBetweenKnownPoints() {
        // Exemplo: São Paulo (lat: -23.5505, lon: -46.6333) para Rio de Janeiro (lat: -22.9068, lon: -43.1729)
        List<Position> positions = new ArrayList<>();
        Position saoPaulo = Position.restore(
                UUID.randomUUID(), UUID.randomUUID(), -23.5505, -46.6333, LocalDateTime.now()
        );
        positions.add(saoPaulo);
        Position rioDeJaneiro = Position.restore(
                UUID.randomUUID(), UUID.randomUUID(), -22.9068, -43.1729, LocalDateTime.now()
        );
        positions.add(rioDeJaneiro);
        double distance = DistanceCalculator.calculateDistance(positions);
        assertTrue(distance >= 350 && distance <= 370, "Distância esperada entre 350 e 370 km");
    }

    @Test
    void testDistanceWithNegativeCoordinates() {
        List<Position> positions = new ArrayList<>();
        Position point1 = Position.restore(
                UUID.randomUUID(), UUID.randomUUID(), -10.0, -10.0, LocalDateTime.now()
        );
        positions.add(point1);
        Position point2 = Position.restore(
                UUID.randomUUID(), UUID.randomUUID(), 10.0, 10.0, LocalDateTime.now()
        );
        positions.add(point2);
        double distance = DistanceCalculator.calculateDistance(positions);
        assertTrue(distance > 0, "Distância deve ser maior que zero");
    }

}