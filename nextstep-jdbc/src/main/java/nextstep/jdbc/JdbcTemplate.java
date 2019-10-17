package nextstep.jdbc;

import nextstep.jdbc.exception.DataBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final String TAG = "JdbcTemplate";

    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> Optional<T> execute(final String sql,
                                   final ResultSetExtractor<T> resultSetExtractor,
                                   final SqlExecuteStrategy sqlExecuteStrategy) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            setValues(sqlExecuteStrategy, preparedStatement);

            return getResult(resultSetExtractor, preparedStatement);

        } catch (SQLException e) {
            logger.error("{}.queryForList >> {}", TAG, e);
            throw new DataBaseException();
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

        return execute(sql, new ObjectMapperResultSetExtractor<>(rowMapper), sqlExecuteStrategy);
    }

    public <T> Optional<T> query(final String sql, final SqlExecuteStrategy sqlExecuteStrategy) {
        return execute(sql, null, sqlExecuteStrategy);
    }

    public <T> List<T> query(final String sql, final RowMapper<T> rowMapper) {
        return execute(sql, new RowMapperResultSetExtractor<>(rowMapper), null)
                .orElse(new ArrayList<>());
    }
}