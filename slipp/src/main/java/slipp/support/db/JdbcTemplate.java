package slipp.support.db;

import nextstep.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public void update(User user) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery())) {
            setValues(user, pstmt);
            pstmt.execute();
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
        }
    }

    protected abstract void setValues(User user, PreparedStatement pstmt) throws SQLException;

    protected abstract String createQuery();
}
