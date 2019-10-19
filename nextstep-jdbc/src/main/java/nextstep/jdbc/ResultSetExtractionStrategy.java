package nextstep.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractionStrategy<T> {
    T extract(ResultSet resultSet) throws SQLException;
}
