package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcMapper<T> {
    T mapped(ResultSet resultSet) throws SQLException;
}
