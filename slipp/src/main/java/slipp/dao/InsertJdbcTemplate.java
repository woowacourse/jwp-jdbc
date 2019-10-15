package slipp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class InsertJdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(InsertJdbcTemplate.class);

    void insert(User user) {
        String sql = createQueryForInsert();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValuesForInsert(user, pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    abstract String createQueryForInsert();

    abstract void setValuesForInsert(User user, PreparedStatement pstmt) throws SQLException;
}
