package nextstep.jdbc.mapper;

import java.sql.ResultSet;
import java.util.List;

public class ResultSetMapper {
    private static final ResultSetMapper INSTANCE = new ResultSetMapper();

    private final List<Mapper> mappers;

    private ResultSetMapper() {
        this.mappers = List.of(new PrimitiveMapper(), new ValueObjectMapper());
    }

    public static ResultSetMapper getInstance(){
        return INSTANCE;
    }

    public <T> T map(final ResultSet rs, final String name, final Class<T> type) {
        return mappers.stream()
                .filter(mapper -> mapper.isMapping(type))
                .findFirst()
                .map(mapper -> mapper.map(rs, name, type))
                .orElseThrow(NotSupportedMapperException::new);
    }
}