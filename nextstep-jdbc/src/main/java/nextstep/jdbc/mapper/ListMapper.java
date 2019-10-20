package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListMapper<T> implements TableMapper<List<T>> {
    private final RowMapper<T> rowMapper;

    public ListMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> create(ResultSet resultSet) throws SQLException {
        List<T> rows = new ArrayList<>();
        while (resultSet.next()) {
            T user = rowMapper.create(resultSet);
            rows.add(user);
        }

        return rows;
    }
}
