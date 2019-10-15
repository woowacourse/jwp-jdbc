package nextstep.jdbc;

import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {
    public void write(final String sql, final PreparedStatementSetter setter) {
        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            setter.setParameters(statement);
            statement.executeUpdate();
        } catch (final SQLException ignored) {}
    }

    public Object findItem(final String sql, final PreparedStatementSetter setter, final RowMapper mapper) {
        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            setter.setParameters(statement);
            return mapper.mapRow(statement.executeQuery());
        } catch (final SQLException ignored) {
            return null;
        }
    }
}
