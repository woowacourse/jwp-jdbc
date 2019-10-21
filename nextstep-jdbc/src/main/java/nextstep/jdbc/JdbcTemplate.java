package nextstep.jdbc;

import nextstep.jdbc.exception.IllegalConnectionException;
import nextstep.jdbc.exception.IllegalExecutionException;
import nextstep.jdbc.preparedstatementsetter.ArgumentPreparedStatementSetter;
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

    public int executeUpdate(String sql, Object... values) {
        return update(sql, values);
    }

    public <T> T executeQuery(String sql, ResultSetMapper<T> resultSetMapper, Object... values) {
        return query(sql, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                T object = null;
                if (resultSet.next()) {
                    object = resultSetMapper.mapRow(resultSet);
                }
                return object;
            }
        }, values);
    }

    public <T> List<T> executeQueryForList(String sql, ResultSetMapper<T> resultSetMapper, Object... values) {
        return query(sql, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> objects = new ArrayList<>();
                while (resultSet.next()) {
                    objects.add(resultSetMapper.mapRow(resultSet));
                }
                return objects;
            }
        }, values);
    }

    public <T> T execute(String sql, ExecuteCallback<T> executeCallback, PreparedStatementSetter preparedStatementSetter) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatementSetter.setValues(preparedStatement);
            return executeCallback.result(preparedStatement);
        } catch (SQLException | IllegalConnectionException e) {
            log.error("execution error : ", e);
            throw new IllegalExecutionException(e);
        }
    }

    private int update(String sql, Object... values) {
        return query(sql, PreparedStatement::executeUpdate, values);
    }

    private <T> T query(String sql, ExecuteCallback<T> statementCallback, Object[] values) {
        return execute(sql, statementCallback, new ArgumentPreparedStatementSetter(values));
    }
}
