package nextstep.jdbc;

// 다른 데에서도 재사용할 수 있는 템플릿을 만들고
// 이를 활용한 userDao를 작성하
// 중복 코드를 삭제하자

import nextstep.util.QueryUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 리팩토링하면서 기존의 클래스가 컴파일 에러가 나면 안 돼
 **/
public class JdbcTemplate implements AutoCloseable {

    private final Connection conn;

    public JdbcTemplate(Connection conn) {
        this.conn = conn;
    }

    public void executeQuery(String query, Map<String, Object> params, ResultSetMapper mapper) {
        try (PreparedStatement pstmt = conn.prepareStatement(QueryUtil.parseQueryParams(query, params));
             ResultSet rs = pstmt.executeQuery()) {
            while(rs.next()) {
                mapper.wrap(rs);
            }
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    @Override
    public void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
