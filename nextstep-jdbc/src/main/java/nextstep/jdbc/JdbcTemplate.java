package nextstep.jdbc;

import nextstep.jdbc.exception.DataAccessException;
import nextstep.jdbc.exception.NotObjectFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public List<T> select(String query, RowMapper<T> rowMapper, Object... values) {
        return select(query, rowMapper, (pstmt -> setValues(pstmt, values)));
    }

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

    public int update(String query, Object... values) {
        return update(query, pstmt -> setValues(pstmt, values));
    }

    public int update(String query, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.setValues(pstmt);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    public T selectForObject(String query, RowMapper<T> rowMapper, Object... values) {
        return selectForObject(query, rowMapper, pstmt -> setValues(pstmt, values));
    }

    public T selectForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.setValues(pstmt);
            return execute(pstmt, rowMapper).orElseThrow(NotObjectFoundException::new);
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    private Optional<T> execute(PreparedStatement pstmt, RowMapper<T> rowMapper) {
        try (ResultSet resultSet = pstmt.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(rowMapper.mapRow(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    private void setValues(PreparedStatement pstmt, Object[] values) throws SQLException {
        for (int i = 1; i <= values.length; i++) {
            pstmt.setObject(i, values[i - 1]);
        }
    }
}
