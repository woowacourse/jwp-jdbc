package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectMapper<T> implements TableMapper<T> {
    private final RowMapper<T> rowMapper;

    public ObjectMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T create(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return rowMapper.create(resultSet);
        }
        return null;
    }
}
