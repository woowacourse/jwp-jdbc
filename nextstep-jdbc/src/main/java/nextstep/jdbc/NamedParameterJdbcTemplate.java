package nextstep.jdbc;

import javax.sql.DataSource;
import java.util.*;

public class NamedParameterJdbcTemplate {
    private static final int DEFAULT_CACHE_LIMIT = 256;

    private final Map<String, NamedParsedSql> namedParsedSqlCache = new LinkedHashMap<>(DEFAULT_CACHE_LIMIT);
    private final JdbcTemplate jdbcTemplate;

    public NamedParameterJdbcTemplate(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int update(final String sql, final Map<String, Object> params) {
        return execute(sql, params, (parsedSql, pss) ->
                jdbcTemplate.update(parsedSql.getOriginSql(), pss));
    }

    public <T> Optional<T> executeForObject(final String sql, final Map<String, Object> params, final RowMapper<T> rowMapper) {
        return execute(sql, params, (parsedSql, pss) ->
                jdbcTemplate.executeForObject(parsedSql.getOriginSql(), pss, rowMapper));
    }

    public <T> List<T> executeForList(final String sql, final RowMapper<T> rowMapper) {
        return this.executeForList(sql, Collections.emptyMap(), rowMapper);
    }

    public <T> List<T> executeForList(final String sql, final Map<String, Object> params, final RowMapper<T> rowMapper) {
        return execute(sql, params, (parsedSql, pss) ->
                jdbcTemplate.executeForList(parsedSql.getOriginSql(), pss, rowMapper));
    }

    public <T> T execute(final String sql, final Map<String, Object> params, final ExecuterCallback<T> action) {
        final NamedParsedSql parsedSql = getNamedParsedSql(sql);
        final Object[] sortedParams = new NamedParameters(params, parsedSql).getParams();
        final PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(sortedParams);
        return action.execute(parsedSql, pss);
    }

    // todo 캐싱 제한 적용
    private NamedParsedSql getNamedParsedSql(final String sql) {
        synchronized (this.namedParsedSqlCache) {
            NamedParsedSql namedParsedSql = namedParsedSqlCache.get(sql);
            if (namedParsedSql == null) {
                namedParsedSql = new NamedParsedSql(sql);
                namedParsedSqlCache.put(sql, namedParsedSql);
            }
            return namedParsedSql;
        }
    }

    interface ExecuterCallback<T> {
        T execute(final NamedParsedSql namedParsedSql, final PreparedStatementSetter pss);
    }
}
