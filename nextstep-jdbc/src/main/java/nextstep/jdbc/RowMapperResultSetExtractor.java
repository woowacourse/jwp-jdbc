package nextstep.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

    private final RowMapper<T> rowMapper;

    public RowMapperResultSetExtractor(final RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<List<T>> extractData(final ResultSet resultSet) throws SQLException {
        List<T> results = new ArrayList<>();

        while (resultSet.next()) {
            results.add(rowMapper.mapRow(resultSet));
        }

        return Optional.of(results);
    }
}
