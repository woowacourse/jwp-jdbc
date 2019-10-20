package nextstep.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NamedParsedSql {
    private static final String DELIMITER = ":";

    private final String originSql;
    private final Map<Integer, Object> params = new TreeMap<>();

    NamedParsedSql(final String sql, final Map<String, Object> params) {
        final StringBuilder sb = new StringBuilder(sql);

        params.forEach((key, value) -> {
            final int start = sb.indexOf(DELIMITER + key);
            sb.replace(start, start + key.length() + 1, "?");
            this.params.put(start, value);
        });
        this.originSql = sb.toString();
    }

    String getOriginSql() {
        return originSql;
    }

    List<Object> getParams() {
        return new ArrayList<>(params.values());
    }
}
