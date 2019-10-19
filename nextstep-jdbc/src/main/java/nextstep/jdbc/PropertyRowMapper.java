package nextstep.jdbc;

import nextstep.jdbc.exception.PropertyRowMapperException;
import nextstep.jdbc.utils.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PropertyRowMapper<T> implements RowMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(PropertyRowMapper.class);

    private final Class<T> clazz;

    private PropertyRowMapper(final Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> PropertyRowMapper<T> from(final Class<T> clazz) {
        return new PropertyRowMapper<>(clazz);
    }

    @Override
    public T mapRow(final ResultSet rs) throws SQLException {
        try {
            final Field[] fields = clazz.getDeclaredFields();
            final T instance = clazz.getConstructor().newInstance();
            for (final Field field : fields) {
                final Object value = TypeParser.parse(rs, field.getName(), field.getType());
                field.setAccessible(true);
                field.set(instance, value);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error(e.getMessage());
            throw new PropertyRowMapperException(e);
        }
    }
}
