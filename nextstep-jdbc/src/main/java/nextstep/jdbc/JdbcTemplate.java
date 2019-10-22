package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int update(String sql, PreparedStatementSetter setter) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.values(pstmt);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int update(String sql, Object... objects) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt, objects);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> T queryForObject(String sql, PreparedStatementSetter setter, RowMapper<T> mapper) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.values(pstmt);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return mapper.mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> mapper, Object... objects) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt, objects);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return mapper.mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> List<T> query(String sql, PreparedStatementSetter setter, RowMapper<T> mapper) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.values(pstmt);
            ResultSet rs = pstmt.executeQuery();
            return getRows(rs, mapper);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> mapper, Object... objects) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt, objects);
            ResultSet rs = pstmt.executeQuery();
            return getRows(rs, mapper);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void setValues(PreparedStatement pstmt, Object... objects) throws SQLException {
        for (int i = 0; i < objects.length; i++) {
            pstmt.setObject(i + 1, objects[i]);
        }
    }

    private <T> List<T> getRows(ResultSet rs, RowMapper<T> mapper) throws SQLException {
        List<T> rows = new ArrayList<>();
        while (rs.next()) {
            rows.add(mapper.mapRow(rs));
        }
        return rows;
    }
}
