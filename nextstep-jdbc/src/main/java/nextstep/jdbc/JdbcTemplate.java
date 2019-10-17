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

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> select(String query, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.setValues(pstmt);
            return getResults(rowMapper, pstmt);
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    private <T> List<T> getResults(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        List<T> results = new ArrayList<>();
        try (ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                results.add(rowMapper.mapRow(resultSet));
            }
        }
        return results;
    }

    public <T> List<T> select(String query, RowMapper<T> rowMapper, Object... values) {
        PreparedStatementSetter pstmtSetter = new VarargsPreparedStatementSetter(values);

        return select(query, rowMapper, pstmtSetter);
    }

    public void update(String query, PreparedStatementSetter pstmtSetter) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    public void update(String query, Object... values) {
        PreparedStatementSetter pstmtSetter = new VarargsPreparedStatementSetter(values);

        update(query, pstmtSetter);
    }

    public <T> T selectForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.setValues(pstmt);
            return execute(pstmt, rowMapper);
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    public <T> T selectForObject(String query, RowMapper<T> rowMapper, Object... values) {
        PreparedStatementSetter pstmtSetter = new VarargsPreparedStatementSetter(values);
        return selectForObject(query, rowMapper, pstmtSetter);
    }

    private <T> T execute(PreparedStatement pstmt, RowMapper<T> rowMapper) {
        try (ResultSet resultSet = pstmt.executeQuery()) {
            if (resultSet.next()) {
                return rowMapper.mapRow(resultSet);
            }
            return null;
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e);
        }
    }
}
