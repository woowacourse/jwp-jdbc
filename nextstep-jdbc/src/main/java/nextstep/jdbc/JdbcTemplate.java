package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    private final DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(final List<String> params, final String sql) {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            setPstmtParams(pstmt, params);

            pstmt.executeUpdate();
        } catch (final SQLException exception) {
            logger.error(exception.toString());
            throw new RuntimeSQLException(exception);
        }
    }

    public <T> T executeQuery(final List<String> params, final String sql, final RowMapper<T> rowMapper) {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql);
             final ResultSet rs = createResultSet(pstmt, params)) {

            return rowMapper.mapRow(rs);
        } catch (final SQLException exception) {
            logger.error(exception.toString());
            throw new RuntimeSQLException(exception);
        }
    }

    private ResultSet createResultSet(final PreparedStatement pstmt, final List<String> params) throws SQLException {
        setPstmtParams(pstmt, params);
        return pstmt.executeQuery();
    }

    private void setPstmtParams(final PreparedStatement pstmt, final List<String> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            pstmt.setString(i + 1, params.get(i));
        }
    }

}
