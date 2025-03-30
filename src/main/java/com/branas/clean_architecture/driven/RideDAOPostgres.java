package com.branas.clean_architecture.driven;

import com.branas.clean_architecture.application.ports.RideRepository;
import com.branas.clean_architecture.driver.RideInput;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class RideDAOPostgres implements RideRepository {

    private final DataSource dataSource;

    public RideDAOPostgres(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Ride getRideById(UUID rideId) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from ride where ride_id = ?");
            ps.setObject(1, rideId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) return new Ride(
                        rs.getObject("ride_id", UUID.class).toString(),
                        rs.getObject("passengerId", UUID.class).toString(),
                        rs.getObject("driver_id", UUID.class).toString(),
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
            throw new RuntimeException("Erro ao buscar a corrida pelo id: " + rideId, e);
        }
        throw new EntityNotFoundException("Corrida não encontrada com o rideId informado");
    }

    @Override
    public List<Ride> getRidesByPassanger(UUID passengerId) {
        List<Ride> rides = new ArrayList<>();
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from ride where passengerId = ?");
            ps.setObject(1, passengerId);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    rides.add(new Ride(
                                    rs.getObject("ride_id", UUID.class).toString(),
                                    rs.getObject("passengerId", UUID.class).toString(),
                                    rs.getObject("driver_id", UUID.class).toString(),
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
            throw new RuntimeException("Erro ao buscar corridas para o passageiro ID: " + passengerId, e);
        }
        return rides;
    }

    @Override
    public UUID saveRide(RideInput rideInput) {
        UUID id = UUID.randomUUID();
        try (Connection con = dataSource.getConnection()) {
            final PreparedStatement insertStatement = con.prepareStatement(
                    "insert into ride " +
                            "(ride_id, passengerId, driver_id, status, fare, from_lat, from_long, to_lat, to_long, distance, date) " +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setObject(1, id);
            insertStatement.setObject(2, rideInput.passengerId());
            insertStatement.setObject(3, null);
            insertStatement.setString(4, Status.SOLICITADA.toString());
            insertStatement.setDouble(5, 0);
            insertStatement.setDouble(6, rideInput.from().latitude());
            insertStatement.setDouble(7, rideInput.from().longitude());
            insertStatement.setDouble(8, rideInput.to().latitude());
            insertStatement.setDouble(9, rideInput.to().longitude());
            insertStatement.setDouble(10,0);
            insertStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted == 0) {
                throw new RuntimeException("Falha ao inserir conta, nenhuma linha foi afetada.");
            }
            return id;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar conta", e);
        }
    }
}
