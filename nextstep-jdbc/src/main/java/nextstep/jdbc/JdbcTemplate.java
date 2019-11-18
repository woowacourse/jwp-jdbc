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

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, SqlExecuteStrategy sqlExecuteStrategy) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            sqlExecuteStrategy.preparedStatementSetter(preparedStatement);
        }
    }

    public <T> Optional<T> readForObject(RowMapper<T> rowMapper, String sql, SqlExecuteStrategy sqlExecuteStrategy) throws SQLException {
        return Optional.of(readForList(rowMapper, sql, sqlExecuteStrategy).get(0));
    }

    public <T> List<T> readForList(RowMapper<T> rowMapper, String sql, SqlExecuteStrategy sqlExecuteStrategy) throws SQLException {
        List<T> objects = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            sqlExecuteStrategy.preparedStatementSetter(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                objects.add(rowMapper.mapRow(resultSet));
            }
        }
        return objects;
    }
}
