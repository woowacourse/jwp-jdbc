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

    public PreparedStatementSetter createPreparedStatementSetter(Object... objects) {
        return pstmt -> {
            for (int i = 0; i < objects.length; ++i) {
                pstmt.setObject(i + 1, objects[i]);
            }
        };
    }

    public void update(String query, Object... objects) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            createPreparedStatementSetter(objects).values(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DataAccessException();
        }
    }

    public List<Object> query(String query, RowMapper rowMapper, PreparedStatementSetter setter) {
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

    public List<Object> query(String query, RowMapper rowMapper, Object... objects) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            createPreparedStatementSetter(objects).values(pstmt);
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

    public Object queryForObject(String query, RowMapper rowMapper, PreparedStatementSetter setter) {
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

    public Object queryForObject(String query, RowMapper rowMapper, Object... objects) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            createPreparedStatementSetter(objects).values(pstmt);
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
