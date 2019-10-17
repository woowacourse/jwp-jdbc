package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private ConnectionManager cm;

    public JdbcTemplate(DBConnection dbConnection) {
        this.cm = new ConnectionManager(dbConnection);
    }

    public void execute(String sql, Object... objects) {
        try (Connection con = cm.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt, objects);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.debug(e.getMessage(), e.getCause());
            throw new DataAccessException();
        }
    }

    public <T> T query(String sql, RowMapper<T> rm) {
        try (Connection con = cm.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                return rm.mapRow(rs);
            }
        } catch (SQLException e) {
            log.debug(e.getMessage(), e.getCause());
            throw new DataAccessException();
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rm, Object... objects) {
        try (Connection con = cm.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt, objects);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rm.mapRow(rs);
            }
        } catch (SQLException e) {
            log.debug(e.getMessage(), e.getCause());
            throw new DataAccessException();
        }
    }

    private void setValues(PreparedStatement pstmt, Object[] objects) throws SQLException {
        for (int i = 0; i < objects.length; i++) {
            pstmt.setObject(i + 1, objects[i]);
        }
    }
}
