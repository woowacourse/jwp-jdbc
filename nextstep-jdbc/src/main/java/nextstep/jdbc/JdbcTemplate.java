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

public class JdbcTemplate<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final String TAG = "JdbcTemplate";

    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<T> execute(String sql, RowMapper<T> rowMapper, SqlExecuteStrategy sqlExecuteStrategy) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            if (sqlExecuteStrategy != null) {
                sqlExecuteStrategy.setValues(preparedStatement);
            }

            if (preparedStatement.execute()) {
                return createList(rowMapper, preparedStatement);
            }

            return null;

        } catch (SQLException e) {
            logger.error("{}.queryForList >> {}", TAG, e);
            throw new DataBaseException();
        }
    }

    public List<T> execute(String sql, SqlExecuteStrategy sqlExecuteStrategy) {
        return execute(sql, null, sqlExecuteStrategy);
    }

    public List<T> execute(String sql, RowMapper<T> rowMapper) {
        return execute(sql, rowMapper, null);
    }

    private List<T> createList(final RowMapper<T> rowMapper, final PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.getResultSet()) {
            List<T> objects = new ArrayList<>();

            while (resultSet.next()) {
                T object = rowMapper.mapRow(resultSet);
                objects.add(object);
            }

            return objects;
        }
    }

    public Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, SqlExecuteStrategy sqlExecuteStrategy) {
        return getObject(execute(sql, rowMapper, sqlExecuteStrategy));
    }

    private Optional<T> getObject(List<T> lists) {
        if (lists.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(lists.get(0));
    }
}