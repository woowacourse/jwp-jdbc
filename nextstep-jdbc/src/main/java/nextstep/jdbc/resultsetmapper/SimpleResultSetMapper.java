package nextstep.jdbc.resultsetmapper;

import nextstep.jdbc.AbstractResultSetMapper;
import nextstep.jdbc.FieldType;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleResultSetMapper<T> extends AbstractResultSetMapper<T> {

    public SimpleResultSetMapper(Class<T> type) {
        super(type);
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        Object object = map(resultSet, super.clazz);
        return clazz.cast(object);
    }

    private Object map(ResultSet resultSet, Class<?> clazz) throws SQLException {
        Object object = instantiate(clazz);
        setFields(resultSet, object);
        return object;
    }

    @Override
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

    @Override
    protected void setField(ResultSet resultSet, Object object, Field field) throws IllegalAccessException, SQLException {
        field.setAccessible(true);
        if (field.get(object) != null) {
            return;
        }
        field.set(object, getObject(resultSet, field));
    }

    @Override
    protected Object getObject(ResultSet resultSet, Field field) throws SQLException {
        Class<?> fieldType = field.getType();
        if (FieldType.isPrimitiveOrWrapped(fieldType)) {
            return resultSet.getObject(field.getName());
        }
        return map(resultSet, fieldType);
    }
}
