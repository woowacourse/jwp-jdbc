package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... args) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            pstmt.executeUpdate();
        }
    }

    public <T> T execute(String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return rowMapper.mapRow(rs);
        }
    }

    public <T> List<T> executeList(String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        List<T> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;
        }
    }
}
