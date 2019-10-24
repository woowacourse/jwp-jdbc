package nextstep.jdbc;

import java.util.Map;
import java.util.TreeMap;

public class NamedParameters {
    private final Object[] params;

    public NamedParameters(final Map<String, Object> params, final NamedParsedSql parsedSql) {
        final Map<Integer, Object> paramMap = new TreeMap<>();
        params.forEach((key, value) -> {
            final int index = parsedSql.getIndexOfParam(key);
            paramMap.put(index, value);
        });
        this.params = paramMap.values().toArray();
    }

    public Object[] getParams() {
        return params;
    }
}
