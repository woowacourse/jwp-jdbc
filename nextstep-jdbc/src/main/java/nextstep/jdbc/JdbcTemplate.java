package nextstep.jdbc;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate<T> {

    private DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public T queryForObject(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) throws SQLException {
        List<T> result = query(sql, pstmtSetter, rowMapper);
        if (result.isEmpty()) {
            throw new SQLException();
        }
        return query(sql, pstmtSetter, rowMapper).get(0);
    }

    public List<T> query(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) throws SQLException {
        ResultSet rs = null;
        List<T> result = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public List<T> query(String sql, RowMapper<T> rowMapper) throws SQLException {
        List<T> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;
        }
    }

    public void update(String sql, PreparedStatementSetter pstmtSetter) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        }
    }

    public void update(String sql, Object... values) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
        }
    }
}
