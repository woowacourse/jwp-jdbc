package slipp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public void update(String query, PreparedStatementSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setter.values(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DataAccessException();
        }
    }

    List<Object> query(String query, RowMapper rowMapper, PreparedStatementSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setter.values(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Object> results = new ArrayList<>();

                while (rs.next()) {
                    results.add(rowMapper.mapRow(rs));
                }
                return results;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DataAccessException();
        }
    }

    Object queryForObject(String query, RowMapper rowMapper, PreparedStatementSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setter.values(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                Object result = null;
                if (rs.next()) {
                    result = rowMapper.mapRow(rs);
                }

                return result;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DataAccessException();
        }
    }
}
