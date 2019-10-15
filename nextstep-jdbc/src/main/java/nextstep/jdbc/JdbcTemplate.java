package nextstep.jdbc;

import nextstep.jdbc.exception.NotOnlyResultException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate<T> {

    public void execute(Connection con, String sql, PreparedStatementSetter pss) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }

   public List<T> query(Connection con, String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException {
        PreparedStatement pstmt = null;
        List<T> result = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(sql);
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet));
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }

        return result;
    }

    public T queryForObject(Connection con, String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException {
        PreparedStatement pstmt = null;
        T result = null;

        try {
            pstmt = con.prepareStatement(sql);
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                if (result != null) {
                    throw new NotOnlyResultException();
                }

                result = rowMapper.mapRow(resultSet);
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }

        return result;
    }
}
