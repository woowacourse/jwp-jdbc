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

    public int update(String sql, PreparedStatementSetter setter) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.values(pstmt);
            return pstmt.executeUpdate();
        }
    }

    public <T> T queryForObject(String sql, PreparedStatementSetter setter, RowMapper<T> mapper) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.values(pstmt);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return mapper.mapRow(rs);
        }
    }

    public <T> List<T> query(String sql, PreparedStatementSetter setter, RowMapper<T> mapper) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.values(pstmt);

            ResultSet rs = pstmt.executeQuery();
            return getRows(rs, mapper);
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
