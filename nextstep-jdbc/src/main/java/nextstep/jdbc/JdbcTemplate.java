package nextstep.jdbc;

import nextstep.jdbc.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    private final DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int update(final String sql, final PreparedStatementSetter pss) {
        return execute(sql, pstmt -> {
            pss.setValues(pstmt);
            return pstmt.executeUpdate();
        });
    }

    public void update(final String sql, final Object... params) {
        this.update(sql, new ArgumentPreparedStatementSetter(params));
    }

    public <T> Optional<T> executeForObject(final String sql, final RowMapper<T> rowMapper) {
        return executeForObject(sql, null, rowMapper);
    }

    public <T> Optional<T> executeForObject(final String sql, final PreparedStatementSetter pss, final RowMapper<T> rowMapper) {
        // ResultSet은 Connection or Statement가 close될 때 자동으로 close 된다.
        return execute(sql, (pstmt) -> {
            if (pss != null) {
                pss.setValues(pstmt);
            }
            final ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(rowMapper.mapRow(rs));
            }
            return Optional.empty();
        });
    }

    public <T> List<T> executeForList(final String sql, final RowMapper<T> rowMapper) {
        return executeForList(sql, null, rowMapper);
    }

    public <T> List<T> executeForList(final String sql, final PreparedStatementSetter pss, final RowMapper<T> rowMapper) {
        return execute(sql, pstmt -> {
            if (pss != null) {
                pss.setValues(pstmt);
            }
            final ResultSet rs = pstmt.executeQuery();
            final List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs));
            }
            return results;
        });
    }

    private <T> T execute(final String sql, final PreparedStatementCallback<T> action) {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {
            return action.doInPreparedStatement(pstmt);
        } catch (final SQLException exception) {
            logger.error(exception.toString());
            throw new DataAccessException(exception);
        }
    }
}
