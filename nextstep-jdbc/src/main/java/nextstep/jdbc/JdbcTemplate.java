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

    public <T> List<T> query(String sql, RowMapper<T> rm) {
        try (Connection con = cm.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                List<T> values = new ArrayList<>();
                while (rs.next()) {
                    values.add(rm.mapRow(rs));
                }
                return values;
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
                if (rs.next()) {
                    return rm.mapRow(rs);
                }
            }
            return null;
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
