package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    T createRow(ResultSet rs) throws SQLException;
}
