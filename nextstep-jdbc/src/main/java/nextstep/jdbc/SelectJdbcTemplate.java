package nextstep.jdbc;

import slipp.domain.User;
import slipp.exception.NotFoundUserException;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class SelectJdbcTemplate {

    public User queryForObject(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(userId, pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return (User) mapRow(rs);
            }
        }

    }

    public List<User> query(String query) throws SQLException {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                List<User> users = (List<User>) mapRow(rs);
                return users;
            }
        }
    }

    public abstract void setValues(String userId, PreparedStatement pstmt) throws SQLException;

    public abstract Object mapRow(ResultSet rs) throws SQLException;

}
