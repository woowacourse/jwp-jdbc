package nextstep.jdbc.executeStrategy;

import nextstep.jdbc.PreparedStatementSetter;
import nextstep.jdbc.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectForObjectStrategy<T> extends AbstractExecuteStrategy<T> {
    private final RowMapper<T> rowMapper;

    public SelectForObjectStrategy(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T handle(PreparedStatement pstmt, PreparedStatementSetter pstmtSetter) throws SQLException {
        pstmtSetter.setValues(pstmt);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            return rowMapper.mapRow(resultSet);
        }
        return null;
    }
}
