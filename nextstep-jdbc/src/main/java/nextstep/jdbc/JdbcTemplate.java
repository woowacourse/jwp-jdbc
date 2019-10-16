package nextstep.jdbc;

import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {
    public void execute(String query, PreparedStatementSetter pss) throws SQLException {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        }
    }

    public User queryForObject(String userId, PreparedStatementSetter pss, RowMapper rm) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return (User) rm.mapRow(rs);
            }
        }
    }

    public List<User> query(String query, RowMapper rm) throws SQLException {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                List<User> users = (List<User>) rm.mapRow(rs);
                return users;
            }
        }
    }

}
