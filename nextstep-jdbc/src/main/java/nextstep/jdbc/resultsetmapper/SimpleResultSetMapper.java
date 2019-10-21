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

    @Override
    protected Object map(ResultSet resultSet, Class<?> clazz) throws SQLException {
        return super.map(resultSet, clazz);
    }

    @Override
    protected void setFields(ResultSet resultSet, Object object) throws SQLException {
        super.setFields(resultSet, object);
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

    @Override
    protected Object instantiate(Class<?> clazz) {
        return super.instantiate(clazz);
    }
}
