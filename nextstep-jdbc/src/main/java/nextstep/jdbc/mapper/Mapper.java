package nextstep.jdbc.mapper;

import java.sql.ResultSet;

public interface Mapper {

    <T> T map(final ResultSet rs, final String name, final Class<T> type);

    boolean isMapping(final Class<?> type);
}
