package nextstep.jdbc;

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
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... values) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = createPreparedStatement(connection, sql, values)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("ErrorCode: {}", e.getErrorCode());
        }
    }

    public Optional<T> readForObject(RowMapper<T> rowMapper, String sql, Object... values) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = createPreparedStatement(connection, sql, values);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return Optional.of(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            log.error("ErrorCode: {}", e.getErrorCode());
        }
        return Optional.empty();
    }

    public List<T> readForList(RowMapper<T> rowMapper, String sql, Object... values) {
        List<T> objects = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = createPreparedStatement(connection, sql, values);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                objects.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            log.error("ErrorCode: {}", e.getErrorCode());
        }
        return objects;
    }

    private PreparedStatement createPreparedStatement(Connection connection, String sql, Object... values) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        int length = values.length;
        for (int i = 0; i < length; i++) {
            preparedStatement.setString(i + 1, values[i].toString());
        }
        return preparedStatement;
    }
}
