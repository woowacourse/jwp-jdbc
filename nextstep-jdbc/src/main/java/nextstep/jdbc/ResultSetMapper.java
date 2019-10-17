package nextstep.jdbc;

import nextstep.jdbc.exception.InstantiationFailedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResultSetMapper<T> {
    private static final String INSTANTIATION_FAILED_EXCEPTION_MESSAGE = "인스턴스 생성 실패";
    private final Class<T> clazz;

    public ResultSetMapper(Class<T> type) {
        this.clazz = type;
    }

    public T mapObject(ResultSet resultSet) throws SQLException {
        return returnFrom(resultSet)
                .orElse(null);
    }

    public List<T> mapList(ResultSet resultSet) throws SQLException {
        List<T> elements = new ArrayList<>();
        addElements(resultSet, elements);
        return elements;
    }

    private void addElements(ResultSet resultSet, List<T> elements) throws SQLException {
        T obj;
        do {
            obj = returnFrom(resultSet)
                    .orElse(null);
            addNotNull(elements, obj);
        } while (obj != null);
    }

    private Optional<T> returnFrom(ResultSet resultSet) throws SQLException {
        return resultSet.next()
                ? Optional.of(clazz.cast(map(resultSet, clazz)))
                : Optional.empty();
    }

    private void addNotNull(List<T> elements, T obj) {
        if (obj != null) {
            elements.add(obj);
        }
    }

    private Object map(ResultSet resultSet, Class<?> clazz) throws SQLException {
        Object object = instantiate(clazz);
        Field[] fields = clazz.getDeclaredFields();
        setFields(resultSet, object, fields);
        return object;
    }

    private void setFields(ResultSet resultSet, Object object, Field[] fields) throws SQLException {
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
