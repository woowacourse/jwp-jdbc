package nextstep.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper {
    Object mapRow(final ResultSet resultSet) throws SQLException;
}
