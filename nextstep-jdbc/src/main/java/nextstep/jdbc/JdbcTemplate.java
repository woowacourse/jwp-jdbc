package nextstep.jdbc;

import nextstep.jdbc.exception.NotOnlyResultException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcTemplate<T> {

    public void execute(String sql, PreparedStatementSetter pss) throws SQLException {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)){
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        }
    }

   public List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException {
        List<T> result = new ArrayList<>();
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)){
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet));
            }
        }

        return result;
    }

    public T queryForObject(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException {
        T result = null;

        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)){
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                if (result != null) {
                    throw new NotOnlyResultException();
                }

                result = rowMapper.mapRow(resultSet);
            }
        }

        return result;
    }

    protected abstract Connection getConnection();
}
