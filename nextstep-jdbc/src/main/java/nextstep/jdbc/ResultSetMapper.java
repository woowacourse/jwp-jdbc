package nextstep.jdbc;

import nextstep.jdbc.exception.InstantiationFailedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMapper<T> {
    private static final String INSTANTIATION_FAILED_EXCEPTION_MESSAGE = "인스턴스 생성 실패";
    private final Class<T> clazz;

    public ResultSetMapper(Class<T> type) {
        this.clazz = type;
    }

    public T mapObject(ResultSet resultSet) throws SQLException, IllegalAccessException {
        T object = null;
        if (resultSet.next()) {
            object = clazz.cast(map(resultSet, clazz));
        }
        return object;
    }

    public List<T> mapList(ResultSet resultSet) throws SQLException, IllegalAccessException {
        List<T> elements = new ArrayList<>();
        while (resultSet.next()) {
            T object = clazz.cast(map(resultSet, clazz));
            elements.add(object);
        }
        return elements;
    }

    private Object map(ResultSet resultSet, Class<?> clazz) throws IllegalAccessException, SQLException {
        Object object = instantiate(clazz);
        Field[] fields = clazz.getDeclaredFields();
        setFields(resultSet, object, fields);
        return object;
    }

    private void setFields(ResultSet resultSet, Object object, Field[] fields) throws IllegalAccessException, SQLException {
        for (Field field : fields) {
            field.setAccessible(true);
            setEmbeddedField(resultSet, object, field);
            setNonDefaultValueField(resultSet, object, field);
        }
    }

    private void setEmbeddedField(ResultSet resultSet, Object object, Field field) throws IllegalAccessException, SQLException {
        if (!FieldType.isPrimitiveOrWrapped(field.getType())) {
            field.set(object, map(resultSet, field.getType()));
        }
    }

    private void setNonDefaultValueField(ResultSet resultSet, Object object, Field field) throws IllegalAccessException, SQLException {
        if (field.get(object) == null) {
            field.set(object, resultSet.getString(field.getName()));
        }
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
