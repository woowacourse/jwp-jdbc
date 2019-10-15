package nextstep.jdbc;

import nextstep.util.QueryUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 리팩토링하면서 기존의 클래스가 컴파일 에러가 나면 안 돼
 **/
public class JdbcTemplate implements AutoCloseable {

    private final Connection conn;

    public JdbcTemplate(Connection conn) {
        this.conn = Objects.requireNonNull(conn);
    }

    public <T> List<T> executeQuery(String query, Map<String, Object> params, ResultSetMapper<T> mapper) {
        try (PreparedStatement pstmt = getStatement(query, params);
             ResultSet rs = pstmt.executeQuery()) {
            return iterateResultSet(mapper, rs);
        } catch (SQLException e) {
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
            throw new JdbcTemplateException(e);
        }
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }
}
