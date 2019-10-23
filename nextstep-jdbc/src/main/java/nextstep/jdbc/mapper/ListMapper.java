package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListMapper<T> implements TableMapper<List<T>> {
    private RowMapper<T> rowMapper;

    public ListMapper(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> mapped(ResultSet resultSet) throws SQLException {
        List<T> rows = new ArrayList<>();
        while (resultSet.next()) {
            T user = rowMapper.createRow(resultSet);
            rows.add(user);
        }

        return rows;
    }
}
