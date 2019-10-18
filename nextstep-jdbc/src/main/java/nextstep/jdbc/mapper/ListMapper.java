package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ListMapper<T> implements TableMapper<List<T>> {

    @Override
    public List<T> create(ResultSet resultSet) throws SQLException {
        List<T> rows = new ArrayList<>();
        while (resultSet.next()) {
            T user = createRow(resultSet);
            rows.add(user);
        }

        return rows;
    }

    protected abstract T createRow(ResultSet resultSet) throws SQLException;
}
