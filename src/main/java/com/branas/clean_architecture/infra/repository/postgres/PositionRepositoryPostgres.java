package com.branas.clean_architecture.infra.repository.postgres;

import com.branas.clean_architecture.application.ports.PositionRepository;
import com.branas.clean_architecture.domain.entity.Position;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class PositionRepositoryPostgres implements PositionRepository {

    private final DataSource dataSource;

    public PositionRepositoryPostgres(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Position savePosition(Position position) {
        try (Connection con = dataSource.getConnection()) {
            final PreparedStatement insertStatement = con.prepareStatement(
                    "INSERT INTO position (position_id, ride_id, lat, long, date) VALUES (?, ?, ?, ?, ?)");
            insertStatement.setObject(1, UUID.fromString(position.getPositionId()));
            insertStatement.setObject(2, UUID.fromString(position.getRideId()));
            insertStatement.setDouble(3, position.getLatitude());
            insertStatement.setDouble(4, position.getLongitude());
            insertStatement.setTimestamp(5, Timestamp.valueOf(position.getDate()));
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted == 0) {
                throw new RuntimeException("Failed to insert position, no rows affected");
            }
            return position;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving position", e);
        }
    }

    @Override
    public List<Position> getPositionsByRideId(String rideId) {
        List<Position> positions = new ArrayList<>();
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from position where ride_id = ?");
            ps.setObject(1, UUID.fromString(rideId));
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    positions.add(Position.restore(
                                    rs.getObject("position_id", UUID.class),
                                    rs.getObject("ride_id", UUID.class),
                                    rs.getDouble("lat"),
                                    rs.getDouble("long"),
                                    rs.getTimestamp("date").toLocalDateTime()
                            )
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get position by ride id: " + rideId, e);
        }
        return positions;

    }

}
