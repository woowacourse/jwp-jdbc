package nextstep.jdbc.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ObjectMapperTest {
    @Test
    @DisplayName("결과가 Object로 매핑되는지 확인")
    void mappedTest() throws Exception {
        RowMapper<Car> rowMapper = rs -> new Car(rs.getString("name"));

        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);

        ObjectMapper<Object> objectMapper = new ObjectMapper<>(rowMapper);
        assertThat(objectMapper.mapped(rs)).isInstanceOf(Car.class);
    }
}