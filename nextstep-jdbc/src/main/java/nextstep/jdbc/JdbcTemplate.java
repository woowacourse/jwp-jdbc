package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class JdbcTemplate {
    private static final PreparedStatementSetter EMPTY_SETTER = statement -> {};
    private final DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T findItem(final String sql, final RowMapper<T> mapper, final Object... parameters) {
        return findItem(sql, mapper, EMPTY_SETTER, parameters);
    }

    public <T> T findItem(final String sql, final RowMapper<T> mapper, final PreparedStatementSetter setter, final Object... parameters) {
        final ResultSet resultSet = getResultSet(sql, setter, parameters);
        return new ResultSetIterator<>(resultSet, mapper).next();
    }

    public <T> List<T> findItems(final String sql, final RowMapper<T> mapper, final Object... parameters) {
        return findItems(sql, mapper, EMPTY_SETTER, parameters);
    }

    public <T> List<T> findItems(final String sql, final RowMapper<T> mapper, final PreparedStatementSetter setter, final Object... parameters) {
        final ResultSet resultSet = getResultSet(sql, setter, parameters);
        final List<T> result = new ArrayList<>();
        for (final ResultSetIterator<T> item = new ResultSetIterator<>(resultSet, mapper); item.hasNext(); ) {
            result.add(item.next());
        }
        return result;
    }

    private ResultSet getResultSet(final String sql, final PreparedStatementSetter setter, final Object... parameters) {
        try {
            return getStatement(sql, setter, parameters).executeQuery();
        } catch (final SQLException exception) {
            throw new DbAccessException(exception);
        }
    }

    public void write(final String sql, final Object... parameters) {
        write(sql, EMPTY_SETTER, parameters);
    }

    public void write(final String sql, final PreparedStatementSetter setter, final Object... parameters) {
        try {
            getStatement(sql, setter, parameters).executeUpdate();
        } catch (final SQLException exception) {
            throw new DbAccessException(exception);
        }
    }

    private PreparedStatement getStatement(final String sql, final PreparedStatementSetter setter, final Object... parameters) throws SQLException {
        final PreparedStatement statement = dataSource.getConnection().prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
        setter.setParameters(statement);
        return statement;
    }

    private static class ResultSetIterator<T> implements Iterator<T> {
        final ResultSet resultSet;
        final RowMapper<T> mapper;
        private T item;

        ResultSetIterator(final ResultSet resultSet, final RowMapper<T> mapper) {
            this.resultSet = resultSet;
            this.mapper = mapper;
            this.item = getNext();
        }

        private T getNext() {
            try {
                if (resultSet.next()) {
                    return mapper.mapRow(resultSet);
                }
                return null;
            } catch (final SQLException e) {
                throw new DbAccessException(e);
            }
        }

        @Override
        public boolean hasNext() {
            return Objects.nonNull(item);
        }

        @Override
        public T next() {
            final T result = item;
            item = getNext();
            return result;
        }
    }
}
