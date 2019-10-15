package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {
    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final DataSource dataSource;

    public void executeUpdate(String sql, PreparedStatementMapping consumer) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();

            pstmt = con.prepareStatement(sql);
            consumer.adjustTo(pstmt);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
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
