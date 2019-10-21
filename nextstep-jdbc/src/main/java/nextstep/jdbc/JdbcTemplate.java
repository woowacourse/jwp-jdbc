package nextstep.jdbc;

import nextstep.jdbc.exception.IllegalConnectionException;
import nextstep.jdbc.exception.IllegalExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T execute(String sql, StatementCallback<T> statementCallback, PreparedStatementSetter preparedStatementSetter) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatementSetter.setValues(preparedStatement);
            return statementCallback.action(preparedStatement);
        } catch (SQLException | IllegalConnectionException e) {
            log.error("execution error : ", e);
            throw new IllegalExecutionException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        return query(sql, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return rowMapper.mapRow(resultSet);
            }
        }, values);
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... values) {
        return query(sql, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> objects = new ArrayList<>();
                while (resultSet.next()) {
                    objects.add(rowMapper.mapRow(resultSet));
                }
                return objects;
            }
        }, values);
    }

    public int update(String sql, Object... values) {
        return query(sql, PreparedStatement::executeUpdate, values);
    }

    public <T> T query(String sql, StatementCallback<T> statementCallback, Object[] values) {
        return execute(sql, statementCallback, new ArgumentPreparedStatementSetter(values));
    }
}
