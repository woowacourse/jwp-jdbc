package slipp.dao;

import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcTemplate {
    public void update(String query) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(query);
            setValues(pstmt);
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

    List<Object> query(String query) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            List<Object> results = new ArrayList<>();

            while (rs.next()) {
                results.add(mapRow(rs));
            }

            return results;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    Object queryForObject(String query) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = query;
            pstmt = con.prepareStatement(sql);
            setValues(pstmt);
            rs = pstmt.executeQuery();

            Object result = null;
            if (rs.next()) {
                result = mapRow(rs);
            }

            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public abstract void setValues(PreparedStatement pstmt) throws SQLException;

    public abstract Object mapRow(ResultSet rs) throws SQLException;
}
