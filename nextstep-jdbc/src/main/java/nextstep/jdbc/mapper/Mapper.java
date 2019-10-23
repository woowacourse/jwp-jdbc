package nextstep.jdbc.mapper;

import java.sql.ResultSet;

public interface Mapper {

    Object map(final ResultSet rs, final String name, final Class<?> type);

    boolean isMapping(final Class<?> type);
}
