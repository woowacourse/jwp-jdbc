package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... values) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = createPreparedStatement(connection, sql, values)) {

            preparedStatement.executeUpdate();
        }
    }

    private PreparedStatement createPreparedStatement(Connection connection, String sql, Object... values) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        int length = values.length;
        for (int i = 0; i < length; i++) {
            preparedStatement.setString(i + 1, values[i].toString());
        }
        return preparedStatement;
    }
}
