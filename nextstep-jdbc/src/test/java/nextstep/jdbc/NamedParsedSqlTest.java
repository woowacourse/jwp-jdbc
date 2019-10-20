package nextstep.jdbc;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class NamedParsedSqlTest {
    private String sql = "UPDATE users SET password = :password, name = :name WHERE userId = :userId";
    private String expected = "UPDATE users SET password = ?, name = ? WHERE userId = ?";
    private Map<String, Object> params = Map.of("userId", "id", "password", "qwe", "name", "name");
    private NamedParsedSql namedParsedSql = new NamedParsedSql(sql, params);

    @Test
    void originSql_추출확() {
        final String actual = namedParsedSql.getOriginSql();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 파라미터_순서_확인() {
        final List<Object> expected = List.of("qwe", "name", "id");
        final List<Object> actual = namedParsedSql.getParams();

        assertThat(actual).isEqualTo(expected);
    }
}