package nextstep.jdbc;

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

    public int update(String query, PreparedStatementSetter setter) {
        return executeQuery(query, pstmt -> {
            setter.values(pstmt);
            return pstmt.executeUpdate();
        });
    }

    public int update(String query, Object... objects) {
        return executeQuery(query, pstmt -> {
            createPreparedStatementSetter(objects).values(pstmt);
            return pstmt.executeUpdate();
        });
    }

    public List<T> query(String query, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        return executeQuery(query, pstmt -> {
            setter.values(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(rowMapper.mapRow(rs));
                }
                return results;
            }
        });
    }

    public List<T> query(String query, RowMapper<T> rowMapper, Object... objects) {
        return executeQuery(query, pstmt -> {
            createPreparedStatementSetter(objects).values(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<T> results = new ArrayList<>();

                while (rs.next()) {
                    results.add(rowMapper.mapRow(rs));
                }
                return results;
            }
        });
    }

    public Object queryForObject(String query, RowMapper rowMapper, PreparedStatementSetter setter) {
        return executeQuery(query, pstmt -> {
            setter.values(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                Object result = null;
                if (rs.next()) {
                    result = rowMapper.mapRow(rs);
                }
                return result;
            }
        });
    }

    public Object queryForObject(String query, RowMapper rowMapper, Object... objects) {
        return executeQuery(query, pstmt -> {
            createPreparedStatementSetter(objects).values(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                Object result = null;
                if (rs.next()) {
                    result = rowMapper.mapRow(rs);
                }
                return result;
            }
        });
    }

    private <T> T executeQuery(String query, JdbcExecutor<T> executor) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            return executor.execute(pstmt);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DataAccessException();
        }
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... objects) {
        return pstmt -> {
            for (int i = 0; i < objects.length; ++i) {
                pstmt.setObject(i + 1, objects[i]);
            }
        };
    }
}
