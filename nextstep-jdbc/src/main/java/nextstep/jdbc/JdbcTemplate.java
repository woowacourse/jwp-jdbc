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

    private static void values(PreparedStatement pstmt) {
    }

    public int update(String query, PreparedStatementSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setter.values(pstmt);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error :", e);
            throw new DataAccessException(e);
        }
    }

    public int update(String query, Object... objects) {
        return update(query, new ObjectSetter(objects));
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setter.values(pstmt);
            return getQueryResults(rowMapper, pstmt);
        } catch (SQLException e) {
            log.error("Error :", e);
            throw new DataAccessException(e);
        }
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

    public <T> List<T> query(String query, RowMapper<T> rowMapper, Object... objects) {
        return query(query, rowMapper, new ObjectSetter(objects));
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper) {
        return query(query, rowMapper, JdbcTemplate::values);
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setter.values(pstmt);
            return getQueryResult(rowMapper, pstmt);
        } catch (SQLException e) {
            log.error("Error :", e);
            throw new DataAccessException(e);
        }
    }

    private <T> T getQueryResult(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
            return null;
        }
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, Object... objects) {
        return queryForObject(query, rowMapper, new ObjectSetter(objects));
    }
}
