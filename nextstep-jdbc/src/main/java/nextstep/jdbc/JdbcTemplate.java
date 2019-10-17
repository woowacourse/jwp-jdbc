package nextstep.jdbc;

import nextstep.utils.QueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 리팩토링하면서 기존의 클래스가 컴파일 에러가 나면 안 돼
 **/
public class JdbcTemplate implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final String TAG = "JdbcTemplate";

    private final Connection conn;

    public JdbcTemplate() {
        this.conn = Objects.requireNonNull(ConnectionManager.getConnection());
    }

    // TODO 메서드명
    public <T> T executeQuery2(String query, Map<String, Object> params, ResultSetMapper<T> mapper) {
        return executeQuery(query, params, mapper).get(0);
    }

    public <T> List<T> executeQuery(String query, Map<String, Object> params, ResultSetMapper<T> mapper) {
        try (PreparedStatement pstmt = getStatement(query, params);
             ResultSet rs = pstmt.executeQuery()) {
            return iterateResultSet(mapper, rs);
        } catch (SQLException e) {
            logger.error("{}.executeQuery() >> {} ", TAG, e);
            throw new JdbcTemplateException(e);
        }
    }

    private PreparedStatement getStatement(String query, Map<String, Object> params) throws SQLException {
        return conn.prepareStatement(QueryUtil.parseQueryParams(query, params));
    }

    private <T> List<T> iterateResultSet(ResultSetMapper<T> mapper, ResultSet rs) throws SQLException {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
            results.add(mapper.wrap(rs));
        }
        return results;
    }

    public int executeUpdate(String query, Map<String, Object> params) {
        try (PreparedStatement pstmt = getStatement(query, params)) {
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("{}.executeUpdate() >> {} ", TAG, e);
            throw new JdbcTemplateException(e);
        }
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            logger.error("{}.close() >> {} ", TAG, e);
            throw new JdbcTemplateException(e);
        }
    }
}
