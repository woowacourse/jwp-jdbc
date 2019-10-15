package nextstep.jdbc;

import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateJdbcTemplate {

    public void update(String sql, User user) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            setValuesForUpdate(pstmt, user);

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

    private void setValuesForUpdate(PreparedStatement pstmt, User user) throws SQLException {
        pstmt.setString(1, user.getPassword());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getEmail());
        pstmt.setString(4, user.getUserId());
    }
}
