package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeUpdate(String sql, PreparedStatementMapping consumer) throws SQLException {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            consumer.adjustTo(pstmt);
            pstmt.executeUpdate();
        }
    }

    public void executeUpdate(String sql) throws SQLException {
        executeUpdate(sql, pstmt -> {
        });
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
