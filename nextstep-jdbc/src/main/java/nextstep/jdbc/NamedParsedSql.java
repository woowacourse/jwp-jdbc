package nextstep.jdbc;

import java.util.*;

public class NamedParsedSql {
    private static final List<Character> SEPARATORS = List.of(')', ',', ' ');
    private static final String DELIMITER = ":";
    private static final String SUBSTITUTION = "?";

    private final String originSql;
    private final Map<String, Integer> indexOfParam = new HashMap<>();

    // todo 리팩토링 어떻게 해야 좋을지 생각
    NamedParsedSql(final String sql) {
        final String[] splitSql = sql.split(DELIMITER);
        final StringBuilder sb = new StringBuilder(splitSql[0]);
        final int length = splitSql.length;

        for (int i = 1; i < length; i++) {
            final String str = splitSql[i];

            final Optional<Integer> result = SEPARATORS.stream()
                    .map(str::indexOf)
                    .filter(x -> x != -1)
                    .findFirst();

            sb.append(SUBSTITUTION);
            if (result.isPresent()) {
                final int index = result.get();
                sb.append(str.substring(index));
                indexOfParam.put(str.substring(0, index), i);
            }

            if (i == length - 1) {
                indexOfParam.put(str, length - 1);
            }
        }
        this.originSql = sb.toString();
    }

    String getOriginSql() {
        return originSql;
    }

    public int getIndexOfParam(final String name) {
        return indexOfParam.get(name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NamedParsedSql that = (NamedParsedSql) o;
        return Objects.equals(originSql, that.originSql);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originSql);
    }
}
