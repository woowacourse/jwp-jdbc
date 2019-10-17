package nextstep.jdbc;

import nextstep.jdbc.exception.*;
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
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final int SETTER_START_INDEX = 1;

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute(String sql, PreparedStatementSetter pss) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new ExecuteUpdateSQLException();
        }
    }

    public void execute(String sql, Object... values) {
        execute(sql, setPreparedStatementFor(values));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        List<T> result = new ArrayList<>();

        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SelectSQLException();
        }

        return result;
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... values) {
        return query(sql, rowMapper, setPreparedStatementFor(values));
    }


    public <T> List<T> queryAll(String sql, RowMapper<T> rowMapper) {
        return query(sql, rowMapper);
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        T result = null;

        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                validNotOnlyResult(result);
                result = rowMapper.mapRow(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SelectSQLException();
        }

        validNoResult(result);
        return result;
    }

    private <T> void validNotOnlyResult(T result) {
        if (result != null) {
            throw new NotOnlyResultException();
        }
    }

    private <T> void validNoResult(T result) {
        if (result == null) {
            throw new NoResultException();
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        return queryForObject(sql, rowMapper, setPreparedStatementFor(values));
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DBConnectException();
        }
    }

    private PreparedStatementSetter setPreparedStatementFor(Object[] values) {
        return pstmt -> {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + SETTER_START_INDEX, values[i]);
            }
        };
    }
}
