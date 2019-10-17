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

    public T mapObject(ResultSet resultSet) throws SQLException {
        T object = null;
        if (resultSet.next()) {
            object = map(resultSet);
        }
        return object;
    }

    public List<T> mapList(ResultSet resultSet) throws SQLException {
        List<T> elements = new ArrayList<>();
        while (resultSet.next()) {
            T object = map(resultSet);
            elements.add(object);
        }
        return elements;
    }

    private T map(ResultSet resultSet) throws SQLException {
        T object = instantiate(clazz);
        setFields(resultSet, object);
        return object;
    }

    private void setFields(ResultSet resultSet, T object) throws SQLException {
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(object, resultSet.getString(field.getName()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private T instantiate(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            throw new InstantiationFailedException(INSTANTIATION_FAILED_EXCEPTION_MESSAGE);
        }
    }
}
