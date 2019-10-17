package nextstep.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementSettingStrategy<T> {
    T handle(PreparedStatement pstmt) throws SQLException;
}
