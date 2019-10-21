package nextstep.jdbc.resultsetmapper;

import nextstep.jdbc.FieldType;
import nextstep.jdbc.ResultSetMapper;
import nextstep.jdbc.exception.InstantiationFailedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleResultSetMapper<T> implements ResultSetMapper<T> {
    private static final String INSTANTIATION_FAILED_EXCEPTION_MESSAGE = "인스턴스 생성 실패";
    private final Class<T> clazz;

    public SimpleResultSetMapper(Class<T> type) {
        this.clazz = type;
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        Object object = map(resultSet, clazz);
        return clazz.cast(object);
    }

    private Object map(ResultSet resultSet, Class<?> clazz) throws SQLException {
        Object object = instantiate(clazz);
        setFields(resultSet, object);
        return object;
    }

    private void setFields(ResultSet resultSet, Object object) throws SQLException {
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                setField(resultSet, object, field);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
        if (FieldType.isPrimitiveOrWrapped(fieldType)) {
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
