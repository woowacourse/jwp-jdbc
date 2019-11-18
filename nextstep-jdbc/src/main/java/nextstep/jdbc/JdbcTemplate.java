package nextstep.jdbc;

import nextstep.utils.QueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 리팩토링하면서 기존의 클래스가 컴파일 에러가 나면 안 돼
 **/
public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final String TAG = "JdbcTemplate";

    private static final int FIRST_INDEX = 0;

    private final DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    public <T> Optional<T> executeQueryForSingleObject(String query, Map<String, Object> params, ResultSetMapper<T> mapper) {
        List<T> results = executeQuery(query, params, mapper);

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(FIRST_INDEX));
    }

    public <T> List<T> executeQuery(String query, Map<String, Object> params, ResultSetMapper<T> mapper) {
        try (PreparedStatement pstmt = getStatement(query, params);
             ResultSet rs = pstmt.executeQuery()) {

            return iterateResultSet(mapper, rs);
        } catch (SQLException e) {
            logger.error("{}.executeQuery() >> ", TAG, e);
            throw new JdbcTemplateException(e);
        }
    }

    private PreparedStatement getStatement(String query, Map<String, Object> params) throws SQLException {
        Connection conn = dataSource.getConnection();
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
            logger.error("{}.executeUpdate() >> ", TAG, e);
            throw new JdbcTemplateException(e);
        }
    }
}
