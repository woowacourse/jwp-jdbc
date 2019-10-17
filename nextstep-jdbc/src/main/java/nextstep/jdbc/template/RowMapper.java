package nextstep.jdbc.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(RowMapper.class);

    private final Class<T> clazz;

    public RowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getInstance(ResultSet resultSet) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            setFields(resultSet, instance);
            return instance;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalArgumentException("Can not create instance");
        }
    }

    private void setFields(ResultSet resultSet, T instance) throws IllegalAccessException, SQLException {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            field.set(instance, resultSet.getString(field.getName()));
        }
    }
}
