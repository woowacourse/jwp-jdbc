package nextstep.jdbc;

import java.util.Map;
import java.util.TreeMap;

public class NamedParsedSql {
    private static final String DELIMITER = ":";
    private static final String SUBSTITUTION = "?";

    private final String originSql;
    private final Map<Integer, Object> params = new TreeMap<>();

    NamedParsedSql(final String sql, final Map<String, Object> params) {
        final StringBuilder sb = new StringBuilder(sql);
        params.forEach((key, value) -> {
            final int start = sb.indexOf(DELIMITER + key);
            final int end = start + key.length() + 1;
            sb.replace(start, end, SUBSTITUTION);
            this.params.put(start, value);
        });
        this.originSql = sb.toString();
    }

    String getOriginSql() {
        return originSql;
    }

    Object[] getParams() {
        return params.values().toArray();
    }
}
