package slipp.mapper;

import nextstep.jdbc.mapper.RowMapper;
import slipp.domain.User;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {
    private static Map<Class<?>, RowMapper> mappers = new HashMap<>();

    static {
        mappers.put(User.class, new UserMapper());
    }

    public <T> RowMapper<T> getMapper(Class<T> type) {
        return mappers.get(type);
    }
}
