package slipp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    void save(String sql) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    abstract void setValues(PreparedStatement pstmt) throws SQLException;
}
