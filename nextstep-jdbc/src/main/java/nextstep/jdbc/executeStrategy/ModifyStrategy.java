package nextstep.jdbc.executeStrategy;

import nextstep.jdbc.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModifyStrategy extends AbstractExecuteStrategy<Integer> {

    @Override
    public Integer handle(PreparedStatement pstmt, PreparedStatementSetter pstmtSetter) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return pstmt.executeUpdate();
    }
}
