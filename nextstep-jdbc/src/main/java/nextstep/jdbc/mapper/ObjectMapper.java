package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ObjectMapper<T> implements TableMapper<T> {

    @Override
    public T mapped(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return createRow(resultSet);
        }
        return null;
    }

    protected abstract T createRow(ResultSet resultSet) throws SQLException;
}
