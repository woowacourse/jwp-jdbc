package nextstep.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleResultSetExtractionStrategy<T> implements ResultSetExtractionStrategy<T> {
    private final RowMapper<T> rowMapper;

    public SingleResultSetExtractionStrategy(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T extract(ResultSet resultSet) throws SQLException {
        return resultSet.next() ? rowMapper.mapRow(resultSet) : null;
    }
}
