package nextstep.jdbc.template;

import nextstep.jdbc.mapper.JdbcMapper;
import nextstep.jdbc.query.JdbcQuery;

public interface DbcTemplate {
    void updateQuery(JdbcQuery query);

    <T> T executeQuery(JdbcQuery query, JdbcMapper<T> mapper);
}
