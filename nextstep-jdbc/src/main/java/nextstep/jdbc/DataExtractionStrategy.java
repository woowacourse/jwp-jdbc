package nextstep.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface DataExtractionStrategy<T> {
    T extract(ResultSet rs) throws SQLException;
}
