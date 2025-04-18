package com.branas.clean_architecture.infra.repository;

import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.domain.ride.Ride;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class RideRepositoryPostgres implements RideRepository {

    private final DataSource dataSource;

    public RideRepositoryPostgres(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Ride getRideById(String rideId) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from ride where ride_id = ?");
            ps.setObject(1, UUID.fromString(rideId));
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) return Ride.restore(
                        rs.getObject("ride_id", UUID.class).toString(),
                        rs.getObject("passenger_id", UUID.class).toString(),
                        rs.getString("status"),
                        rs.getDouble("fare"),
                        rs.getDouble("from_lat"),
                        rs.getDouble("from_long"),
                        rs.getDouble("to_lat"),
                        rs.getDouble("to_long"),
                        rs.getDouble("distance"),
                        rs.getTimestamp("date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get ride id: " + rideId, e);
        }
        throw new EntityNotFoundException("Ride not found");
    }

    @Override
    public List<Ride> getRidesByPassanger(String passengerId) {
        List<Ride> rides = new ArrayList<>();
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from ride where passengerId = ?");
            ps.setObject(1, UUID.fromString(passengerId));
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    rides.add(Ride.restore(
                                    rs.getObject("ride_id", UUID.class).toString(),
                                    rs.getObject("passenger_id", UUID.class).toString(),
                                    rs.getString("status"),
                                    rs.getDouble("fare"),
                                    rs.getDouble("from_lat"),
                                    rs.getDouble("from_long"),
                                    rs.getDouble("to_lat"),
                                    rs.getDouble("to_long"),
                                    rs.getDouble("distance"),
                                    rs.getTimestamp("date").toLocalDateTime()
                            )
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get ride passenger id: " + passengerId, e);
        }
        return rides;
    }

    @Override
    public Ride saveRide(Ride ride) {
        try (Connection con = dataSource.getConnection()) {
            final PreparedStatement insertStatement = con.prepareStatement(
                    "insert into ride " +
                            "(ride_id, passenger_id, driver_id, status, fare, from_lat, from_long, to_lat, to_long, distance, date) " +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setObject(1, UUID.fromString(ride.getRideId()));
            insertStatement.setObject(2, UUID.fromString(ride.getPassengerId()));
            insertStatement.setObject(3, null);
            insertStatement.setString(4, ride.getStatus());
            insertStatement.setDouble(5, 0);
            insertStatement.setDouble(6, ride.getFromLatitude());
            insertStatement.setDouble(7, ride.getFromLongitude());
            insertStatement.setDouble(8, ride.getToLatitude());
            insertStatement.setDouble(9, ride.getToLongitude());
            insertStatement.setDouble(10,0);
            insertStatement.setTimestamp(11, Timestamp.valueOf(ride.getDate()));
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted == 0) {
                throw new RuntimeException("Error saving account, no lines were affected.");
            }
            return ride;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account", e);
        }
    }
}
