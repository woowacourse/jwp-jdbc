package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {
    T createRow(ResultSet resultSet) throws SQLException;
}
