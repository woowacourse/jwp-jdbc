package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void execute(Connection con, String sql, Object... objects) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt, objects);
            pstmt.executeUpdate();
        }
    }

    public <T> List<T> query(Connection con, String sql, RowMapper<T> rm) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            return getListObject(rm, pstmt);
        }
    }

    public <T> T queryForObject(Connection con, String sql, RowMapper<T> rm, Object... objects) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            return getObject(rm, pstmt, objects);
        }
    }

    private <T> T getObject(RowMapper<T> rm, PreparedStatement pstmt, Object[] objects) throws SQLException {
        setValues(pstmt, objects);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rm.mapRow(rs);
            }
        }
        return null;
    }

    private <T> List<T> getListObject(RowMapper<T> rm, PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> values = new ArrayList<>();
            while (rs.next()) {
                values.add(rm.mapRow(rs));
            }
            return values;
        }
    }

    private void setValues(PreparedStatement pstmt, Object[] objects) throws SQLException {
        for (int i = 0; i < objects.length; i++) {
            pstmt.setObject(i + 1, objects[i]);
        }
    }
}
