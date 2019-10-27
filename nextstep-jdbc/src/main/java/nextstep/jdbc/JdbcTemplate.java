package nextstep.jdbc;

import nextstep.jdbc.exception.DataBaseException;
import nextstep.jdbc.exception.IncorrectResultSizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final String TAG = "JdbcTemplate";
    private static final int EMPTY = 0;
    private static final int ONE_OBJECT = 1;

    private static Connection connection;

    public JdbcTemplate(DataSource dataSource) {
        connection = ConnectionManager.initConnection(dataSource);
    }

    public <T> Optional<T> execute(final String sql,
                                   final ResultSetExtractor<T> resultSetExtractor,
                                   final SqlExecuteStrategy sqlExecuteStrategy) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            setValues(sqlExecuteStrategy, preparedStatement);

            return getResult(resultSetExtractor, preparedStatement);

        } catch (SQLException e) {
            logger.error("{}.queryForList >> {}", TAG, e);
            throw new DataBaseException();
        } finally {
            ConnectionManager.closeConnection(connection);
        }
    }

    private void setValues(final SqlExecuteStrategy sqlExecuteStrategy, final PreparedStatement preparedStatement) throws SQLException {
        if (sqlExecuteStrategy != null) {
            sqlExecuteStrategy.setValues(preparedStatement);
        }
    }

    private <T> Optional<T> getResult(final ResultSetExtractor<T> resultSetExtractor, final PreparedStatement preparedStatement) throws SQLException {
        if (preparedStatement.execute()) {
            return extractData(resultSetExtractor, preparedStatement);
        }

        return Optional.empty();
    }

    private <T> Optional<T> extractData(final ResultSetExtractor<T> resultSetExtractor,
                                        final PreparedStatement preparedStatement) throws SQLException {

        try (ResultSet resultSet = preparedStatement.getResultSet()) {
            return resultSetExtractor.extractData(resultSet);
        }
    }

    public <T> Optional<T> query(final String sql,
                                 final RowMapper<T> rowMapper,
                                 final SqlExecuteStrategy sqlExecuteStrategy) {

        List<T> results = execute(sql, new RowMapperResultSetExtractor<>(rowMapper), sqlExecuteStrategy).orElse(new ArrayList<>());

        return getSingleObject(results);
    }

    public <T> Optional<T> queryForCount(final String sql, final RowMapper<T> rowMapper) {
        List<T> results = execute(sql, new RowMapperResultSetExtractor<>(rowMapper), null).orElse(new ArrayList<>());

        return getSingleObject(results);
    }

    private <T> Optional<T> getSingleObject(final List<T> results) {
        if (results.size() == EMPTY) {
            return Optional.empty();
        }

        if (results.size() == ONE_OBJECT) {
            return Optional.of(results.iterator().next());
        }

        throw new IncorrectResultSizeException();
    }

    public <T> Optional<T> query(final String sql, final SqlExecuteStrategy sqlExecuteStrategy) {
        return execute(sql, null, sqlExecuteStrategy);
    }

    public <T> List<T> queryForList(final String sql, final RowMapper<T> rowMapper) {
        return execute(sql, new RowMapperResultSetExtractor<>(rowMapper), null)
                .orElse(new ArrayList<>());
    }
}