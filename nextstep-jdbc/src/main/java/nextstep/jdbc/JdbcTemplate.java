package nextstep.jdbc;

import nextstep.jdbc.exception.JdbcTemplateException;

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

    public void update(String sql, String... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setString(i + 1, args[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // TODO: 2019-10-15 exception 이름 변경
            throw new JdbcTemplateException(e);
        }
    }

    public <T> T executeQuery(String sql, DataExtractionStrategy<T> dataExtractionStrategy, String... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setString(i + 1, args[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                return dataExtractionStrategy.extract(rs);
            }

        } catch (SQLException e) {
            // TODO: 2019-10-15 exception 이름 변경
            throw new JdbcTemplateException(e);
        }
    }
}
