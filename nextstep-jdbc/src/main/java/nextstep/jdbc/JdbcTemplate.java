package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... args) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt, args);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("SQL Exception Thrown : ", e);
            throw new JdbcTemplateException(e);
        }
    }

    public <T> T execute(String sql, RowMapper<T> rowMapper, Object... args) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt, args);

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return rowMapper.mapRow(rs);
        } catch (SQLException e) {
            log.error("SQL Exception Thrown : ", e);
            throw new JdbcTemplateException(e);
        }
    }

    public <T> List<T> executeList(String sql, RowMapper<T> rowMapper, Object... args) {
        List<T> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt, args);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;
        } catch (SQLException e) {
            log.error("SQL Exception Thrown : ", e);
            throw new JdbcTemplateException(e);
        }
    }

    private void setParameters(PreparedStatement pstmt, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
    }
}
