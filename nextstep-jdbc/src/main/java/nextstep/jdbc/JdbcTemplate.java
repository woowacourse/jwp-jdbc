package nextstep.jdbc;

import nextstep.jdbc.exception.JdbcTemplateException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    public <T> List<T> executeQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            List<T> results = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(rowMapper.mapRow(rs));
                }

                return results;
            }

        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    public <T> T executeQueryForSingleObject(String sql, RowMapper<T> rowMapper, Object... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            T result = null;
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = (rowMapper.mapRow(rs));
                }

                return result;
            }

        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }
}
