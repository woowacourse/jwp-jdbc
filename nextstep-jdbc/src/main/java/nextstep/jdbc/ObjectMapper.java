package nextstep.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ObjectMapper<T> implements JdbcMapper {

    @Override
    public T mapped(ResultSet resultSet) throws SQLException {
        T object = null;
        if (resultSet.next()) {
            object = createRow(resultSet);
        }

        return object;
    }

    protected abstract T createRow(ResultSet resultSet) throws SQLException;
}
