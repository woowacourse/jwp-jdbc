package nextstep.jdbc.resultsetextractionstrategy;

import nextstep.jdbc.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleEntityResultSetExtractionStrategy<T> implements ResultSetExtractionStrategy<T> {
    private final RowMapper<T> rowMapper;

    public SingleEntityResultSetExtractionStrategy (RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T extract(ResultSet resultSet) throws SQLException {
        return resultSet.next() ? rowMapper.mapRow(resultSet) : null;
    }
}
