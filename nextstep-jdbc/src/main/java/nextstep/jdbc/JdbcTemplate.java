package nextstep.jdbc;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate<T> {

    private DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<T> queryForObject(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) throws SQLException {
        try {
            List<T> result = query(sql, pstmtSetter, rowMapper);
            return Optional.of(result.get(0));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
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
        return query(sql, pstmt -> {}, rowMapper);
    }

    public void update(String sql, PreparedStatementSetter pstmtSetter) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        }
    }

    public void update(String sql, Object... values) throws SQLException {
        update(sql, pstmt -> {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
        });
    }
}
