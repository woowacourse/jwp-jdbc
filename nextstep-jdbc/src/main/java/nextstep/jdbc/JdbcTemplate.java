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
import java.util.Collections;
import java.util.List;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    private final DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(final String sql, final List<Object> params) {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement pstmt = createPreparedStatement(con, sql, params)) {

            pstmt.executeUpdate();
        } catch (final SQLException exception) {
            logger.error(exception.toString());
            throw new DataAccessException(exception);
        }
    }

    public void update(final String sql, final Object... params) {
        this.update(sql, List.of(params));
    }

    public <T> T executeForObject(final String sql, final RowMapper<T> rowMapper) {
        return executeForObject(sql, Collections.emptyList(), rowMapper);
    }

    public <T> T executeForObject(final String sql, final List<Object> params, final RowMapper<T> rowMapper) {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement pstmt = createPreparedStatement(con, sql, params);
             final ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
            return null;
        } catch (final SQLException exception) {
            logger.error(exception.toString());
            throw new DataAccessException(exception);
        }
    }

    public <T> List<T> executeForList(final String sql, final List<Object> params, final RowMapper<T> rowMapper) {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement pstmt = createPreparedStatement(con, sql, params);
             final ResultSet rs = pstmt.executeQuery()) {

            final List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs));
            }
            return results;
        } catch (final SQLException exception) {
            logger.error(exception.toString());
            throw new DataAccessException(exception);
        }
    }

    private PreparedStatement createPreparedStatement(final Connection con, final String sql, final List<Object> params) throws SQLException {
        final PreparedStatement pstmt = con.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            pstmt.setObject(i + 1, params.get(i));
        }
        return pstmt;
    }
}
