package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    T create(ResultSet resultSet) throws SQLException;
}
