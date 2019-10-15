package slipp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class UpdateJdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(UpdateJdbcTemplate.class);

    void update(User user) {
        String sql = createQueryForUpdate();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValuesForUpdate(user, pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    abstract String createQueryForUpdate();

    abstract void setValuesForUpdate(User user, PreparedStatement pstmt) throws SQLException;
}
