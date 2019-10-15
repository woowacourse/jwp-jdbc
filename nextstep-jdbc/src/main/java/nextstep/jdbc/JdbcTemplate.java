package nextstep.jdbc;

import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class JdbcTemplate {

    public void update(final String sql) {
        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement);
            statement.executeUpdate();
        } catch (final SQLException ignored) {}
    }

    public abstract void setParameters(final PreparedStatement statement) throws SQLException;
}
