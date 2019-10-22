package nextstep.jdbc;

import nextstep.jdbc.exception.FieldSettingFailedException;
import nextstep.jdbc.exception.InstantiationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleRowMapper<T> implements RowMapper<T> {
    private static final Logger log = LoggerFactory.getLogger(SimpleRowMapper.class);

    private static final String INSTANTIATION_FAILED_EXCEPTION_MESSAGE = "인스턴스 생성 실패";
    private final Class<T> clazz;

    public SimpleRowMapper(Class<T> type) {
        this.clazz = type;
    }

    public T mapRow(ResultSet resultSet) throws SQLException {
        Object object = map(resultSet, clazz);
        return clazz.cast(object);
    }

    private Object map(ResultSet resultSet, Class type) throws SQLException {
        Object object = instantiate(type);
        setFields(resultSet, object);
        return object;
    }

    private void setFields(ResultSet resultSet, Object object) throws SQLException {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                setField(resultSet, object, field);
            }
        } catch (IllegalAccessException e) {
            log.error("Field set 에러", e);
            throw new FieldSettingFailedException();
        }
    }

    private void setField(ResultSet resultSet, Object object, Field field) throws IllegalAccessException, SQLException {
        field.setAccessible(true);

        if (field.get(object) != null) {
            return;
        }

        field.set(object, getObject(resultSet, field));
    }

    private Object getObject(ResultSet resultSet, Field field) throws SQLException {
        Class<?> fieldType = field.getType();
        if (FieldType.isPrimitiveOrWrapper(fieldType)) {
            return resultSet.getObject(field.getName());
        }

        return map(resultSet, fieldType);
    }

    private Object instantiate(Class<?> clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            throw new InstantiationFailedException(INSTANTIATION_FAILED_EXCEPTION_MESSAGE);
        }
    }
}
