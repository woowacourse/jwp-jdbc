package nextstep.jdbc.queryexecutor;

import nextstep.jdbc.RowMapper;

import java.sql.PreparedStatement;

public interface JdbcQueryExecutor {
    Boolean canHandle(String sql);
    Object execute(PreparedStatement ps, RowMapper rowMapper);
}
