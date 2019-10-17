package nextstep.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ObjectMapperResultSetExtractor<T> implements ResultSetExtractor<T> {

    private final RowMapper<T> rowMapper;

    public ObjectMapperResultSetExtractor(final RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<T> extractData(final ResultSet resultSet) throws SQLException {

        if (resultSet.next()) {
            return Optional.of(rowMapper.mapRow(resultSet));
        }

        return Optional.empty();
    }
}
