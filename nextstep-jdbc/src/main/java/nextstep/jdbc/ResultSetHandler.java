package nextstep.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetHandler<T> {
    T toObject(ResultSet rs) throws SQLException;
}
