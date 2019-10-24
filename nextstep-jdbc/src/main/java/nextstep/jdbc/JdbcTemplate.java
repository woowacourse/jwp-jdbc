package nextstep.jdbc;

import nextstep.jdbc.exception.SelectQueryException;
import nextstep.jdbc.exception.SqlUpdateException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void update(String sql, PrepareStatementSetter prepareStatementSetter) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            prepareStatementSetter.setParameters(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SqlUpdateException();
        }
    }

    public void update(String sql, Object... args) {
        PrepareStatementSetter prepareStatementSetter = getPrepareStatementSetter(args);
        update(sql, prepareStatementSetter);
    }

    public <T> Optional<T> singleObjectQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        PrepareStatementSetter prepareStatementSetter = getPrepareStatementSetter(args);
        List<T> objects = listQuery(sql, rowMapper, prepareStatementSetter);

        return objects.stream()
            .findFirst();
    }

    public <T> List<T> listQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        PrepareStatementSetter prepareStatementSetter = getPrepareStatementSetter(args);
        return listQuery(sql, rowMapper, prepareStatementSetter);
    }

    private <T> List<T> listQuery(String sql, RowMapper<T> rowMapper, PrepareStatementSetter prepareStatementSetter) {
        List<T> objects = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = getResultSet(sql, prepareStatementSetter, connection)) {
            while (resultSet.next()) {
                objects.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new SelectQueryException();
        }
        return objects;
    }

    private ResultSet getResultSet(String sql, PrepareStatementSetter prepareStatementSetter, Connection con) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        prepareStatementSetter.setParameters(preparedStatement);
        return preparedStatement.executeQuery();
    }

    private PrepareStatementSetter getPrepareStatementSetter(Object[] args) {
        return preparedStatement -> {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
        };
    }
}
