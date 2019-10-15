package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    private final Connection con;

    public JdbcTemplate(Connection con) {
        this.con = con;
    }

    public void updateQuery(UpdateQuery query) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = query.update(con);
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

    public <T> T executeQuery(ExecuteQuery query, JdbcMapper<T> mapper) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = query.execute(con);
            rs = pstmt.executeQuery();
            return mapper.mapped(rs);
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
}
