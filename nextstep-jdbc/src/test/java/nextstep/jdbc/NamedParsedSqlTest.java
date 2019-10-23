package nextstep.jdbc;

import org.junit.jupiter.api.DisplayName;
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
        final List<Object> actual = List.of(namedParsedSql.getParams());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("같은 :key 형태의 두개 존재하는 경우 파싱 확인")
    void parsingTest() {
        // given
        final String inputSql = "INSERT INTO users (password, name) VALUES (:password, :password)";
        final String expectedSql = "INSERT INTO users (password, name) VALUES (?, ?)";
        final Map<String, Object> params = Map.of("password", "pwd");

        // when
        final NamedParsedSql namedParsedSql = new NamedParsedSql(inputSql, params);
        final String actualSql = namedParsedSql.getOriginSql();
        final List<Object> actualParams = List.of(namedParsedSql.getParams());

        // then
        assertThat(actualSql).isEqualTo(expectedSql);
        assertThat(actualParams).hasSize(2);
    }
}