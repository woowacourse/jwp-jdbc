package nextstep.jdbc;

import nextstep.jdbc.exception.DBConnectException;
import nextstep.jdbc.exception.ExecuteUpdateSQLException;
import nextstep.jdbc.exception.NotOnlyResultException;
import nextstep.jdbc.exception.SelectSQLException;

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

    public void execute(String sql, PreparedStatementSetter pss) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
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
                if (result != null) {
                    throw new NotOnlyResultException();
                }

                result = rowMapper.mapRow(resultSet);
            }
        } catch (SQLException e) {
            throw new SelectSQLException();
        }

        return result;
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        return queryForObject(sql, rowMapper, setPreparedStatementFor(values));
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DBConnectException();
        }
    }

    private PreparedStatementSetter setPreparedStatementFor(Object[] values) {
        return pstmt -> {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
        };
    }
}
