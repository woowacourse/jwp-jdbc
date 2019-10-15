package nextstep.jdbc;

import nextstep.jdbc.exception.SQLSelectException;
import nextstep.jdbc.exception.SQLUpdateException;
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
            throw new SQLUpdateException();
        }
    }

    public List<T> queryForList(String sql, RowMapper<T> rowMapper) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<T> objects = new ArrayList<>();

            while (resultSet.next()) {
                T object = rowMapper.mapRow(resultSet);
                objects.add(object);
            }

            return objects;

        } catch (SQLException e) {
            logger.error("{}", e);
            throw new SQLSelectException();
        }
    }

    public T queryForObject(String sql, RowMapper<T> rowMapper, Object... arg) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = setPreparedStatement(connection.prepareStatement(sql), arg);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return rowMapper.mapRow(resultSet);
            }

            return null;

        } catch (SQLException e) {
            logger.error("{}", e);
            throw new SQLSelectException();
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object... arg) throws SQLException {
        for (int i = 0; i < arg.length; i++) {
            preparedStatement.setObject(i + 1, arg[i]);
        }

        return preparedStatement;
    }
}