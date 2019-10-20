package nextstep.jdbc.executeStrategy;

import nextstep.jdbc.PreparedStatementSetter;
import nextstep.jdbc.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectAllStrategy<T> extends AbstractExecuteStrategy<List<T>> {
    private final RowMapper<T> rowMapper;

    public SelectAllStrategy(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> handle(PreparedStatement pstmt, PreparedStatementSetter pstmtSetter) throws SQLException {
        List<T> retrieveObjects = new ArrayList<>();
        ResultSet resultSet = pstmt.executeQuery();
        pstmtSetter.setValues(pstmt);
        while (resultSet.next()) {
            retrieveObjects.add(rowMapper.mapRow(resultSet));
        }
        return retrieveObjects;
    }
}
