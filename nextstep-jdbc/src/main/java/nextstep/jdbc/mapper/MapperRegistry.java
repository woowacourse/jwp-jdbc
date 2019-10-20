package nextstep.jdbc.mapper;

import slipp.domain.User;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {
    private static Map<Class<?>, ObjectMapper> objectMappers = new HashMap<>();
    private static Map<Class<?>, ListMapper> listMappers = new HashMap<>();

    static {
        final UserMapper userMapper = new UserMapper();
        objectMappers.put(User.class, new ObjectMapper(userMapper));
        listMappers.put(User.class, new ListMapper(userMapper));
    }

    public <T> ListMapper<T> getListMapper(Class<T> type) {
        return listMappers.get(type);
    }

    public <T> ObjectMapper<T> getObjectMapper(Class<T> type) {
        return objectMappers.get(type);
    }
}
