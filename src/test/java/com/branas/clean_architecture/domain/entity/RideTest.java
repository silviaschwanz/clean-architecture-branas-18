package com.branas.clean_architecture.domain.entity;

import com.branas.clean_architecture.domain.Status;
import com.branas.clean_architecture.domain.vo.RideStatusAccepted;
import com.branas.clean_architecture.domain.vo.RideStatusInProgress;
import com.branas.clean_architecture.domain.vo.RideStatusRequested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RideTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-01T12:00:00Z");
    private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime.ofInstant(FIXED_INSTANT, ZoneId.systemDefault());
    private Clock fixedClock;

    @BeforeEach
    void setup() {
        fixedClock = Clock.fixed(FIXED_INSTANT, ZoneId.systemDefault());
    }

    @Test
    void shouldCreateNewRide() {
        String passengerId = UUID.randomUUID().toString();
        Ride ride = Ride.create(
                passengerId,
                -25.4271,
                -49.2733,
                -23.5505,
                -46.6333,
                fixedClock
        );
        assertThat(ride.getPassengerId()).isEqualTo(passengerId);
        assertThat(ride.getStatus()).isEqualTo(new RideStatusRequested().getValue());
        assertThat(ride.getFare()).isZero();
        assertThat(ride.getDistance()).isZero();
        assertThat(ride.getDriverId()).isEmpty();
        assertThat(UUID.fromString(ride.getRideId())).isNotNull();
    }

    @Test
    void shouldRestoreRide() {
        UUID rideId      = UUID.randomUUID();
        UUID passengerId = UUID.randomUUID();
        UUID driverId    = UUID.randomUUID();
        Ride ride = Ride.restore(
                rideId,
                passengerId,
                driverId,
                Status.ACCEPTED.toString(),
                57.30,
                -25.0,
                -49.0,
                -23.0,
                -46.0,
                408.2,
                FIXED_DATE_TIME
        );
        assertThat(ride.getRideId()).isEqualTo(rideId.toString());
        assertThat(ride.getPassengerId()).isEqualTo(passengerId.toString());
        assertThat(ride.getDriverId()).isEqualTo(driverId.toString());
        assertThat(ride.getStatus()).isEqualTo(new RideStatusAccepted().getValue());
        assertThat(ride.getFare()).isEqualTo(57.30);
        assertThat(ride.getDistance()).isEqualTo(408.2);
        assertThat(ride.getDate()).isEqualTo(FIXED_DATE_TIME);
    }

    @Test
    void shouldAcceptRideAndAssignDriver() {
        Ride ride = Ride.create(
                UUID.randomUUID().toString(),
                -25.0,
                -49.0,
                -23.0,
                -46.0,
                fixedClock
        );
        String driverId = UUID.randomUUID().toString();
        ride.accept(driverId);
        assertThat(ride.getStatus()).isEqualTo(new RideStatusAccepted().getValue());
        assertThat(ride.getDriverId()).isEqualTo(driverId);
    }

    @Test
    void shouldStartRide() {
        Ride ride = Ride.create(
                UUID.randomUUID().toString(),
                -25.0,
                -49.0,
                -23.0,
                -46.0,
                fixedClock
        );
        ride.accept(UUID.randomUUID().toString());
        ride.start();
        assertThat(ride.getStatus()).isEqualTo(new RideStatusInProgress().getValue());
    }

    @Test
    void shouldReturnEmptyDriverIdIfNotAssigned() {
        Ride ride = Ride.create(
                UUID.randomUUID().toString(),
                -25.0,
                -49.0,
                -23.0,
                -46.0,
                fixedClock
        );
        assertThat("").isEqualTo(ride.getDriverId());
    }

}