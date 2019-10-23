package nextstep.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NamedParsedSqlTest {
    private String sql = "UPDATE users SET password = :password, name = :name WHERE userId = :userId";
    private String expected = "UPDATE users SET password = ?, name = ? WHERE userId = ?";
    private NamedParsedSql namedParsedSql = new NamedParsedSql(sql);

    @Test
    void originSql_추출확인() {
        final String actual = namedParsedSql.getOriginSql();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 파라미터_순서_확인() {
        assertThat(namedParsedSql.getIndexOfParam("password")).isEqualTo(1);
        assertThat(namedParsedSql.getIndexOfParam("name")).isEqualTo(2);
        assertThat(namedParsedSql.getIndexOfParam("userId")).isEqualTo(3);
    }

    @Test
    void 파라미터_없는_일반쿼리_매핑_확인() {
        final String actual = "SELECT * FROM USERS WHERE userId = 'user'";
        final String expected = "SELECT * FROM USERS WHERE userId = 'user'";

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("같은 :key 형태의 두개 존재하는 경우 파싱 확인")
    void parsingTest() {
        // given
        final String inputSql = "INSERT INTO users (password, name) VALUES (:password, :password)";
        final String expectedSql = "INSERT INTO users (password, name) VALUES (?, ?)";

        // when
        final NamedParsedSql namedParsedSql = new NamedParsedSql(inputSql);
        final String actualSql = namedParsedSql.getOriginSql();

        // then
        assertThat(actualSql).isEqualTo(expectedSql);
    }

    @Test
    void 동치성_테스트() {
        final NamedParsedSql namedParsedSql = new NamedParsedSql(sql);

        assertThat(namedParsedSql).isEqualTo(this.namedParsedSql);
    }
}