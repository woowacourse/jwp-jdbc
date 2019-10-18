package nextstep.jdbc.resultsetextractionstrategy;

import nextstep.jdbc.TypeParser;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleEntityResultSetExtractionStrategyWithoutRowMapper<T> implements ResultSetExtractionStrategy<T> {
    private static final Logger log = LoggerFactory.getLogger(SingleEntityResultSetExtractionStrategyWithoutRowMapper.class);

    private final Class<T> clazz;

    public SingleEntityResultSetExtractionStrategyWithoutRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T extract(ResultSet resultSet) {
        try {
            return makeInstance(resultSet);
        } catch (SQLException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error(e.getMessage());
            throw new ExecuteUpdateFailedException(e);
        }
    }

    private T makeInstance(ResultSet resultSet) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        T instance = null;
        if (resultSet.next()) {
            Field[] fields = clazz.getDeclaredFields();
            instance = clazz.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                field.set(instance, TypeParser.parse(resultSet, field.getName(), fieldType));
            }
        }
        return instance;
    }
}