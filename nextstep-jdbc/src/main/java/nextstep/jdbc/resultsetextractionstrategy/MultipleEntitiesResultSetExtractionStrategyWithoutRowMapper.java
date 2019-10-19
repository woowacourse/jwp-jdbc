package nextstep.jdbc.resultsetextractionstrategy;

import nextstep.jdbc.resultsetextractionstrategy.support.TypeParser;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MultipleEntitiesResultSetExtractionStrategyWithoutRowMapper<T> implements ResultSetExtractionStrategy<List<T>> {
    private static final Logger log = LoggerFactory.getLogger(MultipleEntitiesResultSetExtractionStrategyWithoutRowMapper.class);
    private final Class<T> clazz;

    public MultipleEntitiesResultSetExtractionStrategyWithoutRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> extract(ResultSet resultSet) {
        try {
            return makeInstance(resultSet);
        } catch (SQLException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error(e.getMessage());
            throw new ExecuteUpdateFailedException(e);
        }
    }

    private List<T> makeInstance(ResultSet resultSet) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            Field[] fields = clazz.getDeclaredFields();
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                field.set(instance, TypeParser.parse(resultSet, field.getName(), fieldType));
            }
            results.add(instance);
        }
        return results;
    }
}
