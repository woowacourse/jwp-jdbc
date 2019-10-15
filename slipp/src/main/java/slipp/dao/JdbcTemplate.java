package slipp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    void save(User user) {
        String sql = createQuery();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(user, pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    abstract String createQuery();

    abstract void setValues(User user, PreparedStatement pstmt) throws SQLException;
}
