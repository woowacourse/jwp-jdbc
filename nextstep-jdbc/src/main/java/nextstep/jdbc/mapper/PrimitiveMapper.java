package nextstep.jdbc.mapper;

import java.sql.ResultSet;

public class PrimitiveMapper implements Mapper {
    @Override
    public <T> T map(final ResultSet rs, final String name, final Class<T> type) {
        return TypeParser.map(rs, name, type);
    }

    @Override
    public boolean isMapping(final Class<?> type) {
        return TypeParser.isMapping(type);
    }
}
