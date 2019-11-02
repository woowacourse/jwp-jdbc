package nextstep.jdbc;

import nextstep.jdbc.exception.DataAccessException;
import nextstep.jdbc.exception.FailConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            log.error("Fail connection", e);
            throw new FailConnectionException(e);
        }
    }

    public int update(String query, PreparedStatementSetter setter) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setter.values(pstmt);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Fail update", e);
            throw new DataAccessException(e);
        }
    }

    public int update(String query, Object... objects) {
        return update(query, new ObjectSetter(objects));
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setter.values(pstmt);
            return getQueryResults(rowMapper, pstmt);
        } catch (SQLException e) {
            log.error("Fail query :", e);
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
        return query(query, rowMapper, pstmt -> {});
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setter.values(pstmt);
            return getQueryResult(rowMapper, pstmt);
        } catch (SQLException e) {
            log.error("Fail queryForObject :", e);
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
