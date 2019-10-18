package nextstep.jdbc.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class PreparedStatementBuilder {
    private String sql;
    private List<Object> attributes;

    public PreparedStatementBuilder(String sql, Object... parameters) {
        this.sql = sql;
        this.attributes = Arrays.asList(parameters);
    }

    public PreparedStatement create(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql);
        for (int i = 0; i < attributes.size(); i++) {
            pstmt.setObject(i + 1, attributes.get(i));
        }

        return pstmt;
    }
}
