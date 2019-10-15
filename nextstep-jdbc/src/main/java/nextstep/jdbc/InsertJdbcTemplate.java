package nextstep.jdbc;

import slipp.dao.exception.SetValueException;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertJdbcTemplate {

    public void insert(String sql, User user) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            setValuesForInsert(pstmt, user);
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

    private void setValuesForInsert(PreparedStatement pstmt, User user) {
        try {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        } catch (SQLException e) {
            throw new SetValueException();
        }
    }
}
