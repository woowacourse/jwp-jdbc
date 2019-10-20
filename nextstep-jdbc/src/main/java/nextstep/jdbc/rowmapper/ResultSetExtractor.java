package nextstep.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetExtractor {

    private static class LazyHolder {
        private static ResultSetExtractor instance = new ResultSetExtractor();
    }

    public static ResultSetExtractor getInstance() {
        return LazyHolder.instance;
    }

    public <T> T extractSingle(ResultSet resultSet, RowMapper<T> rowMapper) throws SQLException {
        return extract(resultSet, rowMapper).get(0);
    }

    public <T> List<T> extractMultiple(ResultSet resultSet, RowMapper<T> rowMapper) throws SQLException {
        return extract(resultSet, rowMapper);
    }

    public <T> List<T> extract(ResultSet resultSet, RowMapper<T> rowMapper) throws SQLException {
        List<T> results = new ArrayList<>();
        while(resultSet.next()) {
            results.add(rowMapper.mapRow(resultSet));
        }
        return results;
    }
}
