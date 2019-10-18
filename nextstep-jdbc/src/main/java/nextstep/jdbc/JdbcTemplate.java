package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public int update(String query, PreparedStatementSetter setter) {
        return executeQuery(query, pstmt -> {
            setter.values(pstmt);
            return pstmt.executeUpdate();
        });
    }

    public int update(String query, Object... objects) {
        return update(query, new ObjectSetter(objects));
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        return executeQuery(query, pstmt -> {
            setter.values(pstmt);
            return getQueryResults(rowMapper, pstmt);
        });
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper, Object... objects) {
        return query(query, rowMapper, new ObjectSetter(objects));
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper) {
        return executeQuery(query, pstmt -> getQueryResults(rowMapper, pstmt));
    }

    private <T> List<T> getQueryResults(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs));
            }
            return results;
        }
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        return executeQuery(query, pstmt -> {
            setter.values(pstmt);
            return getQueryResult(rowMapper, pstmt);
        });
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, Object... objects) {
        return queryForObject(query, rowMapper, new ObjectSetter(objects));
    }

    private <T> T getQueryResult(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }

            return null;
        }
    }

    private <T> T executeQuery(String query, JdbcExecutor<T> executor) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            return executor.execute(pstmt);
        } catch (SQLException e) {
            log.error("Error :", e);
            throw new DataAccessException(e);
        }
    }
}
