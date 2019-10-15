package nextstep.jdbc;

import nextstep.jdbc.exception.ConnectionFailedException;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);
    private DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionFailedException(e);
        }
    }

    public void executeUpdate(final String sql, Object... values) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int index = 1; index <= values.length; index++) {
                pstmt.setObject(index, values[index - 1]);
                log.debug("executeUpdate value {}={}", index, values[index - 1]);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ExecuteUpdateFailedException(e);
        }
    }
}
