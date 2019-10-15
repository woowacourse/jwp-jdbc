package slipp.support.db;

import nextstep.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateJdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(UpdateJdbcTemplate.class);

    public static void update(User user) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQueryForUpdate())) {
            setValuesForUpdate(user, pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
        }
    }

    private static void setValuesForUpdate(User user, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, user.getPassword());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getEmail());
        pstmt.setString(4, user.getUserId());
    }

    private static String createQueryForUpdate() {
        return "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
    }
}
