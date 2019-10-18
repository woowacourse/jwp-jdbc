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

    public void update(String sql, PreparedStatementSetter setter) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.values(pstmt);
            pstmt.executeUpdate();
        }
    }
}
