package nextstep.jdbc;

import nextstep.jdbc.exception.InstantiationFailedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractResultSetMapper<T> implements ResultSetMapper<T> {
    private static final String INSTANTIATION_FAILED_EXCEPTION_MESSAGE = "인스턴스 생성 실패";
    protected final Class<T> clazz;

    public AbstractResultSetMapper(Class<T> type) {
        this.clazz = type;
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        Object object = map(resultSet, clazz);
        return clazz.cast(object);
    }

    protected Object map(ResultSet resultSet, Class<?> clazz) throws SQLException {
        Object object = instantiate(clazz);
        setFields(resultSet, object);
        return object;
    }

    protected void setFields(ResultSet resultSet, Object object) throws SQLException {
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                setField(resultSet, object, field);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected abstract void setField(ResultSet resultSet, Object object, Field field) throws IllegalAccessException, SQLException;

    protected abstract Object getObject(ResultSet resultSet, Field field) throws SQLException;

    protected Object instantiate(Class<?> clazz) {
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
