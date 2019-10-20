package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectMapper<T> implements JdbcMapper<T> {
    private RowMapper<T> rowMapper;

    public ObjectMapper(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T mapped(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return rowMapper.createRow(resultSet);
        }

        return null;
    }
}
