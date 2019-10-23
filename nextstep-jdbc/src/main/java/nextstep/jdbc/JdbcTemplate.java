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

    public void update(final String sql, final PreparedStatementSetter pss) {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement pstmt = createPreparedStatement(con, sql, pss)) {

            pstmt.execute();
        } catch (final SQLException exception) {
            logger.error(exception.toString());
            throw new DataAccessException(exception);
        }
    }

    public void update(final String sql, final Object... params) {
        this.update(sql, new ArgumentPreparedStatementSetter(params));
    }

    public <T> Optional<T> executeForObject(final String sql, final RowMapper<T> rowMapper) {
//        return executeForObject(sql, Collections.emptyList(), rowMapper);
        return null;
    }

    public <T> Optional<T> executeForObject(final String sql, final PreparedStatementSetter pss, final RowMapper<T> rowMapper) {
        return execute(sql, pss, (rs) -> {
            if (rs.next()) {
                return Optional.of(rowMapper.mapRow(rs));
            }
            return Optional.empty();
        });
    }

    // todo 콜백 패턴으로 수정하기
    public <T> List<T> executeForList(final String sql, final RowMapper<T> rowMapper) {
//        return executeForList(sql, Collections.emptyList(), rowMapper);
        return null;
    }

    public <T> List<T> executeForList(final String sql, final PreparedStatementSetter pss, final RowMapper<T> rowMapper) {
        return execute(sql, pss, (rs) -> {
            final List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs));
            }
            return results;
        });
    }

    private <T> T execute(final String sql, final PreparedStatementSetter pss, final ResultSetExtractor<T> resultSetExtractor) {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement pstmt = createPreparedStatement(con, sql, pss);
             final ResultSet rs = pstmt.executeQuery()) {

            return resultSetExtractor.extractData(rs);
        } catch (final SQLException exception) {
            logger.error(exception.toString());
            throw new DataAccessException(exception);
        }
    }

    private PreparedStatement createPreparedStatement(final Connection con, final String sql, final PreparedStatementSetter pss) throws SQLException {
        final PreparedStatement pstmt = con.prepareStatement(sql);
        pss.setValues(pstmt);
        return pstmt;
    }
}
