package nextstep.jdbc.rowmapper;

import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import nextstep.jdbc.rowmapper.support.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReflectionRowMapper<T> implements  RowMapper<T> {
    private static final Logger log = LoggerFactory.getLogger(ReflectionRowMapper.class);

    private final Class<T> clazz;

    public ReflectionRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        try {
            return makeInstance(resultSet);
        } catch (SQLException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error(e.getMessage());
            throw new ExecuteUpdateFailedException(e);
        }
    }

    private T makeInstance(ResultSet resultSet) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        T instance = null;
        Field[] fields = clazz.getDeclaredFields();
        instance = clazz.getDeclaredConstructor().newInstance();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            field.set(instance, TypeParser.parse(resultSet, field.getName(), fieldType));
        }
        return instance;
    }
}
