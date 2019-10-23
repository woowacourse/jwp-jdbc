package nextstep.jdbc.template;

import nextstep.jdbc.db.ConnectionManager;
import nextstep.jdbc.exception.DatabaseAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private static final int START_SET_VALUE_INDEX = 1;
    private final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    public int save(String sql, Object... objects) {
        return execute(sql, PreparedStatement::executeUpdate, objects);
    }

    private <T> T execute(String sql, PreparedStatementHandler<T> handler, Object... objects) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt, objects);
            return handler.handle(pstmt);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseAccessException(e);
        }
    }

    private void setValues(PreparedStatement pstmt, Object[] objects) {
        for (int index = START_SET_VALUE_INDEX; index <= objects.length; index++) {
            setString(pstmt, objects[index - START_SET_VALUE_INDEX], index);
        }
    }

    private void setString(PreparedStatement pstmt, Object object, int index) {
        try {
            pstmt.setString(index, String.valueOf(object));
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseAccessException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... objects) {
        return execute(sql, pstmt -> ResultSetHelper.getData(rowMapper, pstmt), objects);
    }

    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, Object... objects) {
        return Optional.ofNullable(query(sql, rowMapper, objects).get(0));
    }
}
