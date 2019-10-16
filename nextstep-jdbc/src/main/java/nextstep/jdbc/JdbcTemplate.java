package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeUpdate(String sql) throws SQLException {
        executeUpdate(sql, pstmt -> {
        });
    }

    public void executeUpdate(String sql, PreparedStatementMapping consumer) throws SQLException {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            consumer.adjustTo(pstmt);
            pstmt.executeUpdate();
        }
    }

    public <T> T executeQuery(String sql, ResultSetHandler<T> resultSetHandler) throws SQLException {
        return executeQuery(sql, pstmt -> {
        }, resultSetHandler);
    }

    public <T> T executeQuery(String sql, PreparedStatementMapping mapping, ResultSetHandler<T> resultSetHandler) throws SQLException {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            mapping.adjustTo(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return resultSetHandler.toObject(rs);
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
