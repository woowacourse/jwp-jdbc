package nextstep.jdbc.resultsetextractionstrategy;

import nextstep.jdbc.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MultipleEntitiesResultSetExtractionStrategy<T> implements ResultSetExtractionStrategy<List<T>> {
    private final RowMapper<T> rowMapper;

    public MultipleEntitiesResultSetExtractionStrategy(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;

    }

    @Override
    public List<T> extract(ResultSet resultSet) throws SQLException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            results.add(rowMapper.mapRow(resultSet));
        }
        return results;
    }
}
