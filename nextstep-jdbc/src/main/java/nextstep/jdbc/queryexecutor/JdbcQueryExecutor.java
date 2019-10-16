package nextstep.jdbc.queryexecutor;

import nextstep.jdbc.RowMapper;

import java.sql.PreparedStatement;
import java.util.List;

public interface JdbcQueryExecutor {
    Boolean canHandle(String sql);

    <T> List<T> execute(PreparedStatement ps, RowMapper<T> rowMapper);
}
