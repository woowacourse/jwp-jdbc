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

public class JdbcTemplate<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... arg) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = setPreparedStatement(connection.prepareStatement(sql), arg)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("{}", e);
        }
    }

    public List<T> queryForList(String sql, RowMapper<T> rowMapper) {
        List<T> objects = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                T object = rowMapper.mapRow(resultSet);
                objects.add(object);
            }

        } catch (SQLException e) {
            logger.error("{}", e);
        }

        return objects;
    }

    public T queryForObject(String sql, RowMapper<T> rowMapper, Object... arg) {
        T object = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = setPreparedStatement(connection.prepareStatement(sql), arg);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                object = rowMapper.mapRow(resultSet);
            }

        } catch (SQLException e) {
            logger.error("{}", e);
        }

        return object;
    }

    private PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object... arg) throws SQLException {
        for (int i = 0; i < arg.length; i++) {
            preparedStatement.setObject(i + 1, arg[i]);
        }

        return preparedStatement;
    }
}