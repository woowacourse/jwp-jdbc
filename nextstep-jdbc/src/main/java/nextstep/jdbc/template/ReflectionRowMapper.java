package nextstep.jdbc.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class ReflectionRowMapper<T> implements RowMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(RowMapper.class);
    private static final int START_SET_VALUE_INDEX = 1;

    private final Class<T> clazz;
    private final Map<String, Field> fields;

    public ReflectionRowMapper(Class<T> clazz) {
        this.clazz = clazz;
        this.fields = Arrays.stream(clazz.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .collect(toMap(field -> field.getName().toLowerCase(), identity()));
    }

    public T mapRow(ResultSet resultSet) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            setFields(resultSet, instance);
            return instance;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalArgumentException("Can not create instance");
        }
    }

    private void setFields(ResultSet resultSet, T instance) throws IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = START_SET_VALUE_INDEX; i <= columnCount; i++) {
            Field field = fields.get(metaData.getColumnName(i).toLowerCase());
            setField(resultSet, field);
        }
    }

    private void setField(ResultSet resultSet, Field field) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<?> type = field.getType();
        Method method = ResultSet.class.getMethod(parseMethodName(type.getSimpleName()), String.class);
        method.invoke(resultSet, field.getName());
    }

    private String parseMethodName(String name) {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
