package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public void execute(String query, PreparedStatementSetter pss) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.debug(e.getMessage(), e.getCause());
            throw new DataAccessException();
        }
    }

    public User queryForObject(String userId, PreparedStatementSetter pss, RowMapper<User> rm) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rm.mapRow(rs);
            }
        } catch (SQLException e) {
            log.debug(e.getMessage(), e.getCause());
            throw new DataAccessException();
        }
    }

    public List<User> query(String query, RowMapper<List<User>> rm) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                List<User> users = rm.mapRow(rs);
                return users;
            }
        } catch (SQLException e) {
            log.debug(e.getMessage(), e.getCause());
            throw new DataAccessException();
        }
    }

}
