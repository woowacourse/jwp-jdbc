package nextstep.jdbc;

import nextstep.jdbc.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public List<T> select(String query, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        List<T> list = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
            pstmtSetter.setValues(pstmt);
            while (resultSet.next()) {
                list.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
        return list;
    }

    public List<T> select(String query, RowMapper<T> rowMapper, Object... values) {
        PreparedStatementSetter pstmtSetter = createPreparedStatementSetter(values);

        return select(query, rowMapper, pstmtSetter);
    }

    public void update(String query, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    public void update(String query, Object... values) {
        PreparedStatementSetter pstmtSetter = createPreparedStatementSetter(values);

        update(query, pstmtSetter);
    }

    public T queryForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.setValues(pstmt);
            return execute(pstmt, rowMapper);
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    public T queryForObject(String query, RowMapper<T> rowMapper, Object... values) {
        PreparedStatementSetter pstmtSetter = createPreparedStatementSetter(values);
        return queryForObject(query, rowMapper, pstmtSetter);
    }

    private T execute(PreparedStatement pstmt, RowMapper<T> rowMapper) {
        try (ResultSet resultSet = pstmt.executeQuery()) {
            if (resultSet.next()) {
                return rowMapper.mapRow(resultSet);
            }
            return null;
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... values) {
        return pstmt -> {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
        };
    }
}
