package nextstep.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;

public class ValueObjectMapper implements Mapper {

    @Override
    public <T> T map(final ResultSet rs, final String name, final Class<T> type) {
        try {
            final T instance = type.getConstructor().newInstance();
            final Field field = type.getDeclaredField(name);
            final Object value = TypeParser.map(rs, name, field.getType());
            field.setAccessible(true);
            field.set(instance, value);

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isMapping(final Class<?> type) {
        return type.getDeclaredFields().length == 1;
    }
}