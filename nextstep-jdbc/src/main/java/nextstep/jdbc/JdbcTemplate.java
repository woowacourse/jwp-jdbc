package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final int FIRST_INDEX = 0;

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String query, Object... values) {
        update(query, pstmt -> createPreparedStatementSetter(pstmt, values));
    }

    private void createPreparedStatementSetter(PreparedStatement pstmt, Object... values) throws SQLException {
        for (int index = 0; index < values.length; index++) {
            pstmt.setObject(index + 1, values[index]);
        }
    }

    private void update(String query, PreparedStatementSetter pstmtSetter) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.setPreparedStatement(pstmt);
            pstmt.execute();
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
            throw new DataAccessException("쿼리문을 업데이트 할 수 없습니다!");
        }
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
            throw new DataAccessException("dataSource connection 실패");
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
            throw new DataAccessException("jdbc execute failed!");
        }
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, Object... values) {
        return queryForObject(query, pstmt -> createPreparedStatementSetter(pstmt, values), rowMapper);
    }

    private <T> T queryForObject(String query, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        return Optional.of(query(query, pstmtSetter, rowMapper))
                .filter(list -> list.size() == 1)
                .map(list -> list.get(FIRST_INDEX))
                .orElseThrow(() -> new DataAccessException("데이터가 없거나 여러개 입니다."));
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper, Object... values) {
        return query(query, pstmt -> createPreparedStatementSetter(pstmt, values), rowMapper);
    }

    private <T> List<T> query(String query, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmtSetter.setPreparedStatement(pstmt);
            return execute(pstmt, rowMapper);
        } catch (SQLException e) {
            log.error("SQLException : {}", e.getMessage());
            throw new DataAccessException("데이터가 없습니다!");
        }
    }


}
