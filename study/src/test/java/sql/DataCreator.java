package sql;

import nextstep.jdbc.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DataCreator implements RowMapper<Data> {
    private final String nameKey;
    private final String valueKey;

    public DataCreator(String nameKey, String valueKey) {
        this.nameKey = nameKey;
        this.valueKey = valueKey;
    }

    @Override
    public Data mapRow(ResultSet rs) throws SQLException {
        String name = rs.getString(nameKey);
        Double value = rs.getDouble(valueKey);
        return new Data(name, value);
    }
}
