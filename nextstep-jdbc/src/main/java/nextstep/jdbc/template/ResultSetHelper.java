package nextstep.jdbc.template;

import nextstep.jdbc.exception.DatabaseAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetHelper {
    private static final Logger logger = LoggerFactory.getLogger(ResultSetHelper.class);

    public static <T> List<T> getData(RowMapper<T> rowMapper, PreparedStatement pstmt) {
        try (ResultSet resultSet = pstmt.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(rowMapper.getInstance(resultSet));
            }
            return results;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseAccessException(e);
        }
    }
}
