package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private Connection connection;

    public JdbcTemplate(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("커넥션 실패! : {}", e.getMessage());
        }
    }

    public void insert(String query, Object... params) {
        cxud(query, params);
    }

    public <T> T select(FunctionThrowingSQLException<ResultSet, T> rowMapper, String query, Object... params) {
        try (final ResultSet resultSet = getResultSet(query, params)) {
            if (resultSet.next()) {
                return rowMapper.apply(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new QueryFailedException(e);
        }
    }

    public <T> List<T> selectAll(FunctionThrowingSQLException<ResultSet, T> rowMapper, String query) {
        try (final ResultSet resultSet = getResultSet(query)) {
            List<T> objects = new ArrayList<>();
            while (resultSet.next()) {
                objects.add(rowMapper.apply(resultSet));
            }
            return objects;
        } catch (SQLException e) {
            throw new QueryFailedException(e);
        }
    }

    private ResultSet getResultSet(String query, Object... params) throws SQLException {
        final PreparedStatement pstmt = prepareStatement(query, params);
        return pstmt.executeQuery();
    }

    public void update(String query, Object... params) {
        cxud(query, params);
    }

    public void delete(String query, Object... params) {
        cxud(query, params);
    }

    public void deleteAll(String query) {
        cxud(query);
    }

    private void cxud(String query, Object... params) {
        try (final PreparedStatement pstmt = prepareStatement(query, params)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new QueryFailedException(e);
        }
    }

    private PreparedStatement prepareStatement(
            String query, Object... params
    ) throws SQLException {
        final PreparedStatement pstmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("클로즈 실패! : {}", e.getMessage());
        }
    }
}