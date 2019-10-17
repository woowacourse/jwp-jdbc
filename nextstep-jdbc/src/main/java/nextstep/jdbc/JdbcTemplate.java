package nextstep.jdbc;

import nextstep.jdbc.exception.DatabaseAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... sqlArgs) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            mappingPreparedStatement(pstmt, sqlArgs);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    private <T> T query(String sql, ResultSetHandler<T> resultSetHandler, Object... sqlArgs) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            mappingPreparedStatement(pstmt, sqlArgs);
            try (ResultSet rs = pstmt.executeQuery()) {
                return resultSetHandler.toObject(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, Object... sqlArgs) {
        return queryForObjects(sql, rowMapper, sqlArgs).stream().findFirst();
    }

    public <T> List<T> queryForObjects(String sql, RowMapper<T> rowMapper, Object... sqlArgs) {
        return query(sql, rs -> {
            List<T> objects = new ArrayList<>();
            while (rs.next()) {
                objects.add(rowMapper.toObject(rs));
            }
            return objects;
        }, sqlArgs);
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void mappingPreparedStatement(PreparedStatement pstmt, Object... sqlArgs) {
        int PREPARED_STATEMENT_FIRST_INDEX = 1;
        int ARRAY_FIRST_INDEX = 0;

        IntStream.range(ARRAY_FIRST_INDEX, sqlArgs.length).forEach(index -> {
            try {
                pstmt.setObject(PREPARED_STATEMENT_FIRST_INDEX + index, sqlArgs[index]);
            } catch (SQLException | NullPointerException e) {
                throw new DatabaseAccessException(e);
            }
        });
    }
}
