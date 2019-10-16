package nextstep.jdbc;

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

    public void executeUpdate(String sql) throws SQLException {
        executeUpdate(sql, pstmt -> {
        });
    }

    public void executeUpdate(String sql, Object... objects) throws SQLException { //executeUpdate
        executeUpdate(sql, pstmt -> mappingPreparedStatement(pstmt, objects));
    }

    public void executeUpdate(String sql, PreparedStatementMapping consumer) throws SQLException {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            consumer.adjustTo(pstmt);
            pstmt.executeUpdate();
        }
    }

    public <T> T executeQuery(String sql, ResultSetHandler<T> resultSetHandler) throws SQLException {
        return executeQuery(sql, pstmt -> {
        }, resultSetHandler);
    }

    public <T> T executeQuery(String sql, Object[] objects, ResultSetHandler<T> resultSetHandler) throws SQLException { //executeQuery
        return executeQuery(sql, pstmt -> mappingPreparedStatement(pstmt, objects), resultSetHandler);
    }

    public <T> T executeQuery(String sql, PreparedStatementMapping mapping, ResultSetHandler<T> resultSetHandler) throws SQLException {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            mapping.adjustTo(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return resultSetHandler.toObject(rs);
            }
        }
    }

    public <T> Optional<T> executeQueryForObject(String sql, ObjectMapper<T> objectMapper) throws SQLException {
        return executeQueryForObject(sql, pstmt -> {
        }, objectMapper);
    }

    public <T> Optional<T> executeQueryForObject(String sql, Object[] objects, ObjectMapper<T> objectMapper) throws SQLException { // executeQueryForObject
        return executeQueryForObject(sql, pstmt -> mappingPreparedStatement(pstmt, objects), objectMapper); //executeQuery
    }

    public <T> Optional<T> executeQueryForObject(String sql, PreparedStatementMapping mapping, ObjectMapper<T> objectMapper) throws SQLException {
        return executeQueryForObjects(sql, mapping, objectMapper).stream().findFirst();
    }

    public <T> List<T> executeQueryForObjects(String sql, ObjectMapper<T> objectMapper) throws SQLException {
        return executeQueryForObjects(sql, pstmt -> {
        }, objectMapper);
    }

    public <T> List<T> executeQueryForObjects(String sql, Object[] objects, ObjectMapper<T> objectMapper) throws SQLException { //executeQueryForObject
        return executeQueryForObjects(sql, pstmt -> mappingPreparedStatement(pstmt, objects), objectMapper);
    }

    public <T> List<T> executeQueryForObjects(String sql, PreparedStatementMapping mapping, ObjectMapper<T> objectMapper) throws SQLException {
        return executeQuery(sql, mapping, rs -> {
            List<T> objects = new ArrayList<>();
            while (rs.next()) {
                objects.add(objectMapper.toObject(rs));
            }
            return objects;
        });
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void mappingPreparedStatement(PreparedStatement pstmt, Object[] objects) {
        int PREPARED_STATEMENT_FIRST_INDEX = 1;
        IntStream.rangeClosed(PREPARED_STATEMENT_FIRST_INDEX, objects.length).forEach(index -> {
            try {
                pstmt.setObject(index, objects[index - PREPARED_STATEMENT_FIRST_INDEX]);
            } catch (SQLException | NullPointerException e) {
                e.printStackTrace();
            }
        });
    }
}
