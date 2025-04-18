package com.branas.clean_architecture.application;

import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.application.usecases.GetRide;
import com.branas.clean_architecture.domain.ride.Ride;
import com.branas.clean_architecture.infra.controller.RideOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetRideTest {

    private RideRepository rideRepository;
    private GetRide getRide;

    @BeforeEach
    void setUp() {
        rideRepository = mock(RideRepository.class);
        getRide = new GetRide(rideRepository);
    }

    @Test
    void shouldReturnRideOutputWhenRideExists() {
        String rideId = "b50c2f71-3702-40f7-9484-69cbfc7ebaf1";
        String passengerId = "d9bb8f71-829b-4e01-89f9-8a7a3a1735e0";
        LocalDateTime now = LocalDateTime.now();
        Ride ride = Ride.restore(
                rideId,
                passengerId,
                "requested",
                23.5,
                10.0, -10.0,
                20.0, -20.0,
                15.0,
                now
        );
        when(rideRepository.getRideById(rideId)).thenReturn(ride);
        RideOutput output = getRide.execute(rideId);
        assertEquals(rideId, output.rideId());
        assertEquals(passengerId, output.passengerId());
        assertEquals(10.0, output.fromLatitude());
        assertEquals(-10.0, output.fromLongitude());
        assertEquals(20.0, output.toLatitude());
        assertEquals(-20.0, output.toLongitude());
        assertEquals("requested", output.status());
        verify(rideRepository).getRideById(rideId);
    }

    @Test
    void shouldThrowExceptionWhenRideNotFound() {
        String rideId = "invalid-id";
        when(rideRepository.getRideById(rideId)).thenReturn(null);
        Exception exception = assertThrows(NullPointerException.class, () -> {
            getRide.execute(rideId);
        });
        assertNotNull(exception);
        verify(rideRepository).getRideById(rideId);
    }

}