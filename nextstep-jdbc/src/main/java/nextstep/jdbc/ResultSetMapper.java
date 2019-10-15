package nextstep.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetMapper {
    void wrap(ResultSet resultSet) throws SQLException;
}
