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
        List<T> result = query(sql, rowMapper, pstmtSetter);
        if (result.isEmpty()) {
            return null;
        }
        return query(sql, rowMapper, pstmtSetter).get(0);
    }

    public List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, sql, pstmtSetter);
             ResultSet rs = pstmt.executeQuery()) {
            List<T> result = new ArrayList<>();

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;
        }
    }

    public List<T> query(String sql, RowMapper<T> rowMapper, Object... objects) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, sql, objects);
             ResultSet rs = pstmt.executeQuery()) {
            List<T> result = new ArrayList<>();

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;
        }
    }

    private PreparedStatement createPreparedStatement(Connection con, String sql, PreparedStatementSetter pstmtSetter) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmtSetter.setValues(pstmt);
        return pstmt;
    }

    private PreparedStatement createPreparedStatement(Connection con, String sql, Object... objects) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql);

        for (int i = 0; i < objects.length; i++) {
            pstmt.setObject(i + 1, objects[i]);
        }
        return pstmt;
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
