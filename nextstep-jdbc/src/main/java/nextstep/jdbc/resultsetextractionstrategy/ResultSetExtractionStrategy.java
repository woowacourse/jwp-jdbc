package nextstep.jdbc.resultsetextractionstrategy;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetExtractionStrategy<U> {
    U extract(ResultSet resultSet) throws SQLException, NoSuchMethodException;
}
