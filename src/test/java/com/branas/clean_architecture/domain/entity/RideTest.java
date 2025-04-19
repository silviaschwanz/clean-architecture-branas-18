package com.branas.clean_architecture.domain.entity;

import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.vo.RideStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RideTest {

    private String passengerId;
    private double fromLat, fromLong, toLat, toLong;

    @BeforeEach
    void setup() {
        passengerId = UUID.randomUUID().toString();
        fromLat = -25.0;
        fromLong = -49.0;
        toLat = -26.0;
        toLong = -48.0;
    }

    @Test
    void shouldCreateNewRide() {
        Ride ride = Ride.create(passengerId, fromLat, fromLong, toLat, toLong);
        assertNotNull(ride.getRideId());
        assertEquals(passengerId, ride.getPassengerId());
        assertEquals(fromLat, ride.getFromLatitude());
        assertEquals(fromLong, ride.getFromLongitude());
        assertEquals(toLat, ride.getToLatitude());
        assertEquals(toLong, ride.getToLongitude());
        assertEquals(Status.REQUESTED.toString(), ride.getStatus());
        assertEquals(0, ride.getFare());
        assertEquals(0, ride.getDistance());
    }

    @Test
    void shouldRestoreRide() {
        UUID rideId = UUID.randomUUID();
        UUID passId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        double fare = 35.0;
        double distance = 10.5;
        LocalDateTime date = LocalDateTime.now();
        Ride ride = Ride.restore(
                rideId,
                passId,
                driverId,
                Status.ACCEPTED.toString(),
                fare,
                fromLat, fromLong, toLat, toLong,
                distance,
                date
        );
        assertEquals(rideId.toString(), ride.getRideId());
        assertEquals(passId.toString(), ride.getPassengerId());
        assertEquals(driverId.toString(), ride.getDriverId());
        assertEquals(Status.ACCEPTED.toString(), ride.getStatus());
        assertEquals(fare, ride.getFare());
        assertEquals(distance, ride.getDistance());
        assertEquals(date, ride.getDate());
    }

    @Test
    void shouldAcceptRideAndAssignDriver() {
        Ride ride = Ride.create(passengerId, fromLat, fromLong, toLat, toLong);
        RideStatus mockedStatus = mock(RideStatus.class);
        ride.setStatus(mockedStatus);
        String driverId = UUID.randomUUID().toString();
        ride.accept(driverId);
        verify(mockedStatus).accept();
        assertEquals(driverId, ride.getDriverId());
    }

    @Test
    void shouldStartRide() {
        Ride ride = Ride.create(passengerId, fromLat, fromLong, toLat, toLong);
        RideStatus mockedStatus = mock(RideStatus.class);
        ride.setStatus(mockedStatus);
        ride.start();
        verify(mockedStatus).start();
    }

    @Test
    void shouldReturnEmptyDriverIdIfNotAssigned() {
        Ride ride = Ride.create(passengerId, fromLat, fromLong, toLat, toLong);
        assertEquals("", ride.getDriverId());
    }

}