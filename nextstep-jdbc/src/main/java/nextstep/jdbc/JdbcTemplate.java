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

    public void insert(String query, String... params) {
        insertOrUpdate(query, params);
    }

    public void update(String query, String... params) {
        insertOrUpdate(query, params);
    }

    private void insertOrUpdate(String query, String... params) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = prepareInsertOrUpdateStatement(con, query, params)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private PreparedStatement prepareInsertOrUpdateStatement(
            Connection con, String query, String... params
    ) throws SQLException {
        final PreparedStatement pstmt = con.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            pstmt.setString(i + 1, params[i]);
        }
        return pstmt;
    }
}