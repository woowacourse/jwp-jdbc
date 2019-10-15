package slipp.support.db;

import nextstep.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public void update(String query) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            setValues(pstmt);
            pstmt.execute();
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
        }
    }

    protected abstract void setValues(PreparedStatement pstmt) throws SQLException;
}
