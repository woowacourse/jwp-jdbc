package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final int FIRST_INDEX = 0;

    public void update(String query, Object... values) {
        update(query, pstmt -> createPreparedStatementSetter(pstmt, values));
    }

    private void update(String query, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.values(pstmt);
            pstmt.execute();
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
            throw new DataAccessException("질의를 수행할 수 없습니다.");
        }
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, Object... values) {
        return queryForObject(query, pstmt -> createPreparedStatementSetter(pstmt, values), rowMapper);
    }

    private <T> T queryForObject(String query, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.values(pstmt);
            return execute(pstmt, rowMapper).get(FIRST_INDEX);
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
            throw new DataAccessException("데이터를 찾을 수 없습니다.");
        }
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper, Object... values) {
        return query(query, pstmt -> createPreparedStatementSetter(pstmt, values), rowMapper);
    }

    private <T> List<T> query(String query, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.values(pstmt);
            return execute(pstmt, rowMapper);
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
            throw new DataAccessException("데이터를 찾을 수 없습니다.");
        }
    }

    private <T> List<T> execute(PreparedStatement pstmt, RowMapper<T> rowMapper) {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rowMapper.mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
            throw new DataAccessException("데이터를 찾을 수 없습니다.");
        }
    }

    private void createPreparedStatementSetter(PreparedStatement pstmt, Object[] values) throws SQLException {
        if (values == null) {
            return;
        }

        for (int index = 0; index < values.length; index++) {
            pstmt.setObject(index + 1, values[index]);
        }
    }
}
