package nextstep.jdbc;

import nextstep.jdbc.exception.ConnectionFailedException;
import nextstep.jdbc.exception.ExecuteQueryFailedException;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
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

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final int START_INDEX = 1;
    private DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ConnectionFailedException(e);
        }
    }

    public void executeUpdate(final String sql, Object... values) {
        log.debug("executeUpdate sql={}", sql);

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValuesToPreparedStatement(pstmt, values);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ExecuteUpdateFailedException(e);
        }
    }

    public <T> List<T> executeQuery(final String sql, RowMapper<T> rowMapper, Object... values) {
        log.debug("executeQuery sql={}", sql);

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValuesToPreparedStatement(pstmt, values);

            return executeRowMapper(pstmt, rowMapper);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ExecuteQueryFailedException(e);
        }
    }

    public <T> Optional<T> executeQueryWithUniqueResult(final String sql, RowMapper<T> rowMapper, Object... values) {
        log.debug("executeQueryWithUniqueResult sql={}", sql);

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValuesToPreparedStatement(pstmt, values);

            return Optional.ofNullable(executeRowMapperWithUniqueResult(pstmt, rowMapper));
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ExecuteQueryFailedException(e);
        }
    }

    private void setValuesToPreparedStatement(final PreparedStatement pstmt, final Object[] values) throws SQLException {
        for (int index = START_INDEX; index <= values.length; index++) {
            pstmt.setObject(index, values[index - 1]);
        }
    }

    private <T> List<T> executeRowMapper(final PreparedStatement pstmt, final RowMapper<T> rowMapper) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> objects = new ArrayList<>();
            while (rs.next()) {
                objects.add(rowMapper.mapRow(rs));
            }
            return objects;
        }
    }

    private <T> T executeRowMapperWithUniqueResult(final PreparedStatement pstmt, final RowMapper<T> rowMapper) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            return (rs.next()) ? rowMapper.mapRow(rs) : null;
        }
    }
}
