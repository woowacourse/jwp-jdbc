package nextstep.utils;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class QueryUtilTest {

    @Test
    void parse() {
        // given
        String query = "SELECT * FROM questions WHERE id=:id AND name=:name";
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", 1);
        params.put("name", "에단");

        // when
        String parsedQuery = QueryUtil.parseQueryParams(query, params);

        // then
        assertThat(parsedQuery).isEqualTo("SELECT * FROM questions WHERE id='1' AND name='에단'");
    }
}