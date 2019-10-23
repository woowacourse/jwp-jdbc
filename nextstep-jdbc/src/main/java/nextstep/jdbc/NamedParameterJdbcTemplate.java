package nextstep.jdbc;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NamedParameterJdbcTemplate {
    private final JdbcTemplate jdbcTemplate;

    public NamedParameterJdbcTemplate(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void update(final String sql, final Map<String, Object> params) {
        final NamedParsedSql parsedSql = new NamedParsedSql(sql, params);
        jdbcTemplate.update(parsedSql.getOriginSql(), parsedSql.getParams());
    }

    public <T> Optional<T> executeForObject(final String sql, final Map<String, Object> params, final RowMapper<T> rowMapper) {
        final NamedParsedSql parsedSql = new NamedParsedSql(sql, params);
        return jdbcTemplate.executeForObject(parsedSql.getOriginSql(), parsedSql.getParams(), rowMapper);
    }

    public <T> List<T> executeForList(final String sql, final RowMapper<T> rowMapper) {
        return this.executeForList(sql, Collections.emptyMap(), rowMapper);
    }

    public <T> List<T> executeForList(final String sql, final Map<String, Object> params, final RowMapper<T> rowMapper) {
        final NamedParsedSql parsedSql = new NamedParsedSql(sql, params);
        return jdbcTemplate.executeForList(parsedSql.getOriginSql(), parsedSql.getParams(), rowMapper);
    }
}
