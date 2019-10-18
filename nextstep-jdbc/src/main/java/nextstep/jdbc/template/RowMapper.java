package nextstep.jdbc.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class RowMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(RowMapper.class);
    private static final int START_SET_VALUE_INDEX = 1;

    private final Class<T> clazz;
    private final Map<String, Field> fields;

    public RowMapper(Class<T> clazz) {
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

    private void setFields(ResultSet resultSet, T instance) throws IllegalAccessException, SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = START_SET_VALUE_INDEX; i <= columnCount; i++) {
            Field field = fields.get(metaData.getColumnName(i).toLowerCase());
            field.set(instance, resultSet.getString(field.getName()));
        }
    }
}
