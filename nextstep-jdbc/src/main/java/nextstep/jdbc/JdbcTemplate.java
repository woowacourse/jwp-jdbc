package nextstep.jdbc;

import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public <T> T findItem(final String sql, final RowMapper<T> mapper, final Object... parameters) {
        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            setPreparedStatements(statement, parameters);
            final ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? mapper.mapRow(resultSet) : null;
        } catch (final SQLException exception) {
            throw new DbAccessException(exception);
        }
    }

    public <T> List<T> findItems(final String sql, final RowMapper<T> mapper) {
        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            final ResultSet resultSet = statement.executeQuery();
            final List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(mapper.mapRow(resultSet));
            }
            return result;
        } catch (final SQLException exception) {
            throw new DbAccessException(exception);
        }
    }

    public void write(final String sql, final Object... parameters) {
        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            setPreparedStatements(statement, parameters);
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw new DbAccessException(exception);
        }
    }

    private void setPreparedStatements(final PreparedStatement statement, final Object[] parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
