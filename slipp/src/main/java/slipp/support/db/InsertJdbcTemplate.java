package slipp.support.db;

import nextstep.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.dao.UserDao;
import slipp.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertJdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(InsertJdbcTemplate.class);

    public static void insert(User user) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQueryForInsert())) {
            setValuesForInsert(user, pstmt);
            pstmt.execute();
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
        }
    }

    private static void setValuesForInsert(User user, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, user.getUserId());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getName());
        pstmt.setString(4, user.getEmail());
    }

    private static String createQueryForInsert() {
        return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    }
}
